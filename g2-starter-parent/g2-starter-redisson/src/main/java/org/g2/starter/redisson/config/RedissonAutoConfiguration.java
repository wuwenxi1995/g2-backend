package org.g2.starter.redisson.config;

import org.g2.starter.redisson.config.responsibility.AbstractServerConfig;
import org.g2.starter.redisson.lock.config.RedissonConfigureProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@Configuration
@EnableConfigurationProperties(value = {RedissonConfigureProperties.class})
@ComponentScan(basePackages = "org.g2.starter.redisson.lock")
public class RedissonAutoConfiguration {

    @Bean(initMethod = "init")
    @ConditionalOnProperty(prefix = "g2.lock.redisson", value = "enable", havingValue = "true")
    @ConditionalOnBean(AbstractServerConfig.class)
    public RedissonBuildFactory redissonBuildFactory(RedissonConfigureProperties properties) {
        return new RedissonBuildFactory(properties);
    }

    @Bean(
            destroyMethod = "shutdown"
    )
    @ConditionalOnBean(RedissonBuildFactory.class)
    public RedissonClient redissonClient(RedissonBuildFactory redissonBuildFactory) {
        return redissonBuildFactory.build();
    }

    @Bean(
            destroyMethod = "shutdown"
    )
    @ConditionalOnBean(RedissonBuildFactory.class)
    public RedissonClient lockRedissonClient(RedissonBuildFactory redissonBuildFactory) {
        return redissonBuildFactory.build();
    }
}
