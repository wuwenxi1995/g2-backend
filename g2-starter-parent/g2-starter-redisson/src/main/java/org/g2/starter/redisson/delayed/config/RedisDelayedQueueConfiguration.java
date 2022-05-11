package org.g2.starter.redisson.delayed.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2021-09-15
 */
@Configuration
@ConditionalOnProperty(prefix = "g2.redis.delayed", value = "isEnable", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackages = "org.g2.starter.redisson.delayed.infra")
public class RedisDelayedQueueConfiguration {
}
