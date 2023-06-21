package org.g2.starter.message.config;

import org.g2.starter.message.config.endpoint.RedisMessageListenerEndpointRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wuwenxi 2022-12-12
 */
public class RedisMessageListenerSelector implements ImportBeanDefinitionRegistrar {

    private static final String REDIS_MESSAGE_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.g2.message.config.RedisMessageListenerBeanPostProcessor";
    private static final String REDIS_MESSAGE_LISTENER_ANNOTATION_REGISTRY_BEAN_NAME =
            "org.g2.message.config.endpoint.RedisMessageListenerEndpointRegistry";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(REDIS_MESSAGE_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            registry.registerBeanDefinition(REDIS_MESSAGE_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME, new RootBeanDefinition(RedisMessageListenerBeanPostProcessor.class));
        }

        if (!registry.containsBeanDefinition(REDIS_MESSAGE_LISTENER_ANNOTATION_REGISTRY_BEAN_NAME)) {
            registry.registerBeanDefinition(REDIS_MESSAGE_LISTENER_ANNOTATION_REGISTRY_BEAN_NAME, new RootBeanDefinition(RedisMessageListenerEndpointRegistry.class));
        }
    }
}
