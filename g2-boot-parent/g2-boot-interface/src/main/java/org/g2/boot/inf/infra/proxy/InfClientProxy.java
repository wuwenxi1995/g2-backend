package org.g2.boot.inf.infra.proxy;

import org.apache.commons.lang3.StringUtils;
import org.g2.boot.inf.annotation.Call;
import org.g2.boot.inf.data.InfResConf;
import org.g2.boot.inf.data.InfSysConf;
import org.g2.boot.inf.infra.exception.InfClientException;
import org.g2.boot.inf.infra.executor.InfClientExecutor;
import org.g2.boot.inf.infra.metadata.MethodMetadata;
import org.g2.boot.inf.infra.resolver.InfClientClassResolver;
import org.g2.starter.core.base.BaseConstants;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author wuwenxi 2021-06-24
 */
public class InfClientProxy implements InvocationHandler {

    private Class<?> beanClass;
    private ApplicationContext applicationContext;


    public InfClientProxy(Class<?> beanClass, ApplicationContext applicationContext) {
        this.beanClass = beanClass;
        this.applicationContext = applicationContext;
    }

    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanClass);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Call call = AnnotationUtils.findAnnotation(method, Call.class);
        if (null == call) {
            return method.invoke(proxy, args);
        }
        InfClientClassResolver infClientClassResolver = applicationContext.getBean(InfClientClassResolver.class);
        MethodMetadata methodMetadata = infClientClassResolver.resolveMethod(beanClass, method);
        RedisCacheClient redisCacheClient = applicationContext.getBean(RedisCacheClient.class);
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
        InfClientExecutor infClientExecutor = applicationContext.getBean(InfClientExecutor.class);
        return infClientExecutor.execute(infSysConf, infResConf, methodMetadata, args);
    }
}
