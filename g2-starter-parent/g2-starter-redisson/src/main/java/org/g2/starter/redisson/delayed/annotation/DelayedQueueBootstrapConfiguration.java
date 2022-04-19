package org.g2.starter.redisson.delayed.annotation;

import org.g2.starter.redisson.delayed.annotation.registry.DelayedListenerEndpointRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wuwenxi 2021-12-27
 */
public class DelayedQueueBootstrapConfiguration implements ImportBeanDefinitionRegistrar {

    private static final String DELAYED_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME = "org.g2.starter.redisson.annotation.internalDelayedListenerPostProcessor";
    public static final String DELAYED_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME = "org.g2.starter.redisson.annotation.internalDelayedListenerEndpointRegistry";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 注册后置处理器
        if (!registry.containsBeanDefinition(DELAYED_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            registry.registerBeanDefinition(DELAYED_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME,
                    new RootBeanDefinition(DelayedListenerAnnotationPostProcessor.class));
        }

        if (!registry.containsBeanDefinition(DELAYED_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)) {
            registry.registerBeanDefinition(DELAYED_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME,
                    new RootBeanDefinition(DelayedListenerEndpointRegistry.class));
        }
    }
}
