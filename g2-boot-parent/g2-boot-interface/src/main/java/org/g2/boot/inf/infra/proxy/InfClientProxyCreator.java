
package org.g2.boot.inf.infra.proxy;

import org.apache.commons.lang3.StringUtils;
import org.g2.boot.inf.annotation.Call;
import org.g2.boot.inf.annotation.InfClient;
import org.g2.boot.inf.data.InfResConf;
import org.g2.boot.inf.data.InfSysConf;
import org.g2.boot.inf.infra.exception.InfClientException;
import org.g2.boot.inf.infra.executor.InfClientExecutor;
import org.g2.boot.inf.infra.metadata.MethodMetadata;
import org.g2.boot.inf.infra.resolver.InfClientClassResolver;
import org.g2.core.base.BaseConstants;
import org.g2.starter.redis.client.RedisCacheClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-16
 */
public class InfClientProxyCreator implements InstantiationAwareBeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(InfClientProxyCreator.class);

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (shouldSkip(beanClass)) {
            if (!beanClass.isInterface()) {
                log.warn("Skip creating a proxy , {} has @InfClient but it's not an interface", beanClass.getSimpleName());
                return null;
            }
            // 创建代理对象
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            InfClientProxy infClientProxy = new InfClientProxy(beanClass, applicationContext);
            enhancer.setCallback(infClientProxy);
            return enhancer.create();
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, String beanName) throws BeansException {
        return bean;
    }

    private boolean shouldSkip(Class<?> beanClass) {
        InfClient infClient = AnnotationUtils.findAnnotation(beanClass, InfClient.class);
        return infClient != null && infClient.createProxy();
    }

    private static class InfClientProxy implements InvocationHandler {

        private Class<?> beanClass;
        private ApplicationContext applicationContext;
        private volatile InfClientExecutor infClientExecutor;
        private volatile InfClientClassResolver infClientClassResolver;
        private volatile RedisCacheClient redisCacheClient;


        InfClientProxy(Class<?> beanClass, ApplicationContext applicationContext) {
            this.beanClass = beanClass;
            this.applicationContext = applicationContext;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Call call = AnnotationUtils.findAnnotation(method, Call.class);
            if (null == call) {
                return method.invoke(proxy, args);
            }
            if (infClientClassResolver == null) {
                infClientClassResolver = applicationContext.getBean(InfClientClassResolver.class);
            }
            MethodMetadata methodMetadata = infClientClassResolver.resolveMethod(beanClass, method);
            if (redisCacheClient == null) {
                redisCacheClient = applicationContext.getBean(RedisCacheClient.class);
            }
            String infResConfStr = redisCacheClient.<String, String>boundHashOps(InfResConf.cacheKey(methodMetadata.getCode())).get("data");
            if (StringUtils.isBlank(infResConfStr)) {
                throw new InfClientException(String.format("[%s] interface client config not found", methodMetadata.getCode()));
            }
            InfResConf infResConf = InfResConf.valueOf(infResConfStr);
            if (BaseConstants.Flag.NO.equals(infResConf.getActive())) {
                throw new InfClientException(String.format("[%s] interface client config is not active", methodMetadata.getCode()));
            }
            String infSysConfStr = redisCacheClient.<String, String>boundHashOps(InfResConf.cacheKey(infResConf.getSysCode())).get("data");
            if (StringUtils.isBlank(infSysConfStr)) {
                throw new InfClientException(String.format("[%s] interface client config not found", methodMetadata.getCode()));
            }
            InfSysConf infSysConf = InfSysConf.valueOf(infSysConfStr);
            if (infClientExecutor == null) {
                infClientExecutor = applicationContext.getBean(InfClientExecutor.class);
            }
            return infClientExecutor.execute(infSysConf, infResConf, methodMetadata, args);
        }
    }
}
