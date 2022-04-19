package org.g2.starter.redisson.delayed.annotation;

import org.g2.starter.redisson.delayed.annotation.config.DelayedListenerThreadProperties;
import org.g2.starter.redisson.delayed.annotation.registry.DelayedListenerEndpoint;
import org.g2.starter.redisson.delayed.annotation.registry.DelayedListenerEndpointRegistrar;
import org.g2.starter.redisson.delayed.annotation.registry.DelayedListenerEndpointRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuwenxi 2021-12-27
 */
public class DelayedListenerAnnotationPostProcessor implements BeanPostProcessor, Ordered,
        ApplicationContextAware, SmartInitializingSingleton {

    private final Logger logger = LoggerFactory.getLogger(DelayedListenerAnnotationPostProcessor.class);

    private final Set<Class<?>> noAnnotationBeans = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    private DelayedListenerEndpointRegistrar registrar = new DelayedListenerEndpointRegistrar();

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (applicationContext instanceof ConfigurableApplicationContext) {
            setBeanFactory(((ConfigurableApplicationContext) applicationContext).getBeanFactory());
        } else {
            setBeanFactory(applicationContext);
        }
    }

    private void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 所有单实例bean初始化完成，进行回调
     */
    @Override
    public void afterSingletonsInstantiated() {
        this.registrar.setBeanFactory(beanFactory);
        if (this.registrar.getEndpointRegistry() == null) {
            DelayedListenerEndpointRegistry endpointRegistry = this.applicationContext.getBean(DelayedQueueBootstrapConfiguration.DELAYED_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME, DelayedListenerEndpointRegistry.class);
            this.registrar.setEndpointRegistry(endpointRegistry);
        }
        if (this.registrar.getDefaultExecutorBeanName() == null) {
            this.registrar.setDefaultExecutorBeanName(DelayedListenerThreadProperties.DELAYED_LISTENER_DEFAULT_THREAD);
        }
        this.registrar.afterPropertiesSet();
    }

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (!noAnnotationBeans.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Map<Method, DelayedListener> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<DelayedListener>) method -> AnnotationUtils.findAnnotation(method, DelayedListener.class));
            if (annotatedMethods.isEmpty()) {
                this.noAnnotationBeans.add(bean.getClass());
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("No @DelayedListener annotations found on bean type: {}", bean.getClass());
                }
            } else {
                for (Map.Entry<Method, DelayedListener> entry : annotatedMethods.entrySet()) {
                    DelayedListenerEndpoint endpoint = new DelayedListenerEndpoint();
                    endpoint.setBean(bean);
                    endpoint.setBeanName(beanName);
                    endpoint.setMethod(entry.getKey());
                    endpoint.setDelayedListener(entry.getValue());
                    endpoint.setBeanFactory(this.beanFactory);
                    this.registrar.registerEndpoint(endpoint);
                }
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("{} @DelayedListener methods processed on bean '{}' : {}", annotatedMethods.size(), beanName, annotatedMethods);
                }
            }
        }
        return bean;
    }
}