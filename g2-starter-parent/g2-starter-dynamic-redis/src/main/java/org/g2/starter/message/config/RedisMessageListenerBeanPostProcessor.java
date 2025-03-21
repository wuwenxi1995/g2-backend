package org.g2.starter.message.config;

import org.g2.starter.message.annotation.RedisMessageListener;
import org.g2.starter.message.config.endpoint.MethodRedisMessageListenerEndpoint;
import org.g2.starter.message.config.endpoint.RedisMessageListenerEndpointRegistrar;
import org.g2.starter.message.config.endpoint.RedisMessageListenerEndpointRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wuwenxi 2022-12-06
 */
public class RedisMessageListenerBeanPostProcessor implements BeanPostProcessor, SmartInitializingSingleton, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(RedisMessageListenerBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    private Set<Class<?>> nonAnnotatedClasses = new HashSet<>();

    private RedisMessageListenerEndpointRegistrar registrar = new RedisMessageListenerEndpointRegistrar();

    @Override
    public void afterSingletonsInstantiated() {
        RedisMessageListenerEndpointRegistry registry = this.applicationContext.getBean(RedisMessageListenerEndpointRegistry.class);
        this.registrar.setEndpointRegistry(registry);
        // 创建监听器
        this.registrar.afterPropertiesSet();
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (!nonAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Map<Method, RedisMessageListener> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<RedisMessageListener>) method -> AnnotatedElementUtils.findMergedAnnotation(method, RedisMessageListener.class));
            if (annotatedMethods.isEmpty()) {
                nonAnnotatedClasses.add(bean.getClass());
                log.trace("No @RedisMessageListener annotations found on bean type: " + bean.getClass());
            } else {
                for (Map.Entry<Method, RedisMessageListener> entry : annotatedMethods.entrySet()) {
                    Method method = entry.getKey();
                    RedisMessageListener listener = entry.getValue();
                    processRedisMessageListener(listener, method, bean);
                }
                log.debug(annotatedMethods.size() + " @RedisMessageListener methods processed on bean '"
                        + beanName + "': " + annotatedMethods);
            }
        }
        return bean;
    }

    private void processRedisMessageListener(RedisMessageListener listener, Method method, Object bean) {
        Method methodToUse = checkProxy(method, bean);
        if (methodToUse.getParameters().length != 1) {
            throw new IllegalArgumentException(String.format(
                    "@RedisMessageListener method '%s' found on bean target class '%s', " +
                            "and at least one parameter at most, but found parameter '%s'",
                    method.getName(), bean.getClass().getSimpleName(), methodToUse.getParameters().length));
        }
        MethodRedisMessageListenerEndpoint listenerEndpoint = new MethodRedisMessageListenerEndpoint();
        listenerEndpoint.setMethod(methodToUse);
        listenerEndpoint.setType(methodToUse.getParameters()[0].getType());
        listenerEndpoint.setBean(bean);
        listenerEndpoint.setDb(listener.db());
        listenerEndpoint.setQueueName(listener.queue());
        this.registrar.registerEndpoint(listenerEndpoint);
    }

    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                // Found a @RedisMessageListener method on the target class for this JDK proxy ->
                // is it also present on the proxy itself?
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    } catch (@SuppressWarnings("unused") NoSuchMethodException noMethod) {
                        // NOSONAR
                    }
                }
            } catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                        "@RedisMessageListener method '%s' found on bean target class '%s', " +
                                "but not found in any interface(s) for bean JDK proxy. Either " +
                                "pull the method up to an interface or switch to subclass (CGLIB) " +
                                "proxies by setting proxy-target-class/proxyTargetClass " +
                                "attribute to 'true'", method.getName(),
                        method.getDeclaringClass().getSimpleName()), ex);
            }
        }
        return method;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
