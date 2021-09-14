package org.g2.starter.redis.mq.config;

import org.g2.starter.redis.mq.config.processor.MqBeanDefinitionRegisterProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
@Configuration
@EnableConfigurationProperties(RedisMqConfigurationProperties.class)
public class EnableMqAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "g2.redis.mq", value = "enable", havingValue = "true")
    public MqBeanDefinitionRegisterProcessor mqBeanDefinitionRegisterProcessor(RedisMqConfigurationProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setKeepAliveSeconds(properties.getKeepAlive());
        executor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());
        executor.setThreadNamePrefix("redis-listener-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.afterPropertiesSet();
        return new MqBeanDefinitionRegisterProcessor(executor);
    }
}
