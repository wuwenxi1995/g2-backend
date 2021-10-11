package org.g2.starter.redisson.delayed.config;

import org.g2.starter.redisson.delayed.RedisDelayedQueue;
import org.g2.starter.redisson.delayed.process.DelayedQueueProcessing;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2021-09-15
 */
@Configuration
@ConditionalOnProperty(prefix = "g2.redis.delayed", value = "isEnable", havingValue = "true", matchIfMissing = true)
public class RedisDelayedQueueConfiguration {

    @Bean
    @ConditionalOnBean(RedisDelayedQueue.class)
    public DelayedQueueProcessing redisDelayedQueueInit() {
        return new DelayedQueueProcessing();
    }
}
