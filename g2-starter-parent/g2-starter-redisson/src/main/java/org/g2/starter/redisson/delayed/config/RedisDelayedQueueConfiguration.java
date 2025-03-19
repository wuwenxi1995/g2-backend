package org.g2.starter.redisson.delayed.config;

import org.g2.starter.redisson.config.RedissonAutoConfiguration;
import org.g2.starter.redisson.delayed.infra.RedisDelayedQueue;
import org.g2.starter.redisson.delayed.infra.handler.DelayedQueueHandler;
import org.g2.starter.redisson.delayed.infra.listener.DelayedMessageListener;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wuwenxi 2021-09-15
 */
@Configuration
@ConditionalOnProperty(prefix = "g2.redis.delayed", value = "isEnable", havingValue = "true", matchIfMissing = true)
@Import(RedissonAutoConfiguration.class)
public class RedisDelayedQueueConfiguration {

    @Bean
    @ConditionalOnBean(RedisDelayedQueue.class)
    public DelayedQueueHandler delayedQueueHandler(RedissonClient redissonClient, RedisDelayedQueue redisDelayedQueue) {
        return new DelayedQueueHandler(redissonClient, redisDelayedQueue);
    }

    @Bean
    @ConditionalOnBean(DelayedMessageListener.class)
    public RedisDelayedQueue redisDelayedQueue() {
        return new RedisDelayedQueue();
    }
}
