package org.g2.starter.redisson.lock.autoconfigure;

import org.g2.starter.redisson.config.RedissonAutoConfiguration;
import org.g2.starter.redisson.lock.config.RedissonConfigureProperties;
import org.g2.starter.redisson.lock.infra.service.impl.FairLockStrategy;
import org.g2.starter.redisson.lock.infra.service.impl.MultiLockStrategy;
import org.g2.starter.redisson.lock.infra.service.impl.ReadLockStrategy;
import org.g2.starter.redisson.lock.infra.service.impl.RedLockStrategy;
import org.g2.starter.redisson.lock.infra.service.impl.ReentrantLockStrategy;
import org.g2.starter.redisson.lock.infra.service.impl.WriteLockStrategy;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@Configuration
@EnableConfigurationProperties(value = {RedissonConfigureProperties.class})
@ComponentScan(basePackages = "org.g2.starter.redisson.lock")
@Import(RedissonAutoConfiguration.class)
public class LockAutoConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnBean(name = "lockRedissonClient")
    public FairLockStrategy fairLockStrategy() {
        return new FairLockStrategy();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnBean(name = "lockRedissonClient")
    public ReentrantLockStrategy reentrantLockStrategy() {
        return new ReentrantLockStrategy();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnBean(name = "lockRedissonClient")
    public MultiLockStrategy multiLockStrategy() {
        return new MultiLockStrategy();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnBean(name = "lockRedissonClient")
    public ReadLockStrategy readLockStrategy() {
        return new ReadLockStrategy();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnBean(name = "lockRedissonClient")
    public RedLockStrategy redLockStrategy() {
        return new RedLockStrategy();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnBean(name = "lockRedissonClient")
    public WriteLockStrategy writeLockStrategy() {
        return new WriteLockStrategy();
    }
}
