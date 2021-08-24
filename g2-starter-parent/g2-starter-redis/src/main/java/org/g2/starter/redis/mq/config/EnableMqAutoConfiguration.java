package org.g2.starter.redis.mq.config;

import org.g2.starter.redis.mq.config.processor.MqBeanDefinitionRegisterProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
@Configuration
@EnableConfigurationProperties(RedisMqConfigurationProperties.class)
public class EnableMqAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "g2.redis.mq", value = "enable", havingValue = "true")
    public MqBeanDefinitionRegisterProcessor mqBeanDefinitionRegisterProcessor() {
        return new MqBeanDefinitionRegisterProcessor();
    }
}
