package org.g2.core.aop.config;

import org.g2.core.aop.config.processor.InterceptorRegistryPostProcessor;
import org.g2.core.aop.config.properties.InterceptorConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2021-07-29
 */
@Configuration
@EnableConfigurationProperties(InterceptorConfigurationProperties.class)
public class InterceptorConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "g2.starter.interceptor", value = "enable", havingValue = "true")
    public InterceptorRegistryPostProcessor interceptorRegistryPostProcessor() {
        return new InterceptorRegistryPostProcessor();
    }
}
