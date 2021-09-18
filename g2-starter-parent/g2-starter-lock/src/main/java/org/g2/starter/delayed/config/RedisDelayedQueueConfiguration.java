package org.g2.starter.delayed.config;

import org.g2.starter.delayed.RedisDelayedQueue;
import org.g2.starter.delayed.process.DelayedQueueProcessing;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2021-09-15
 */
@Configuration
public class RedisDelayedQueueConfiguration {

    @Bean
    @ConditionalOnBean(RedisDelayedQueue.class)
    public DelayedQueueProcessing redisDelayedQueueInit() {
        return new DelayedQueueProcessing();
    }
}
