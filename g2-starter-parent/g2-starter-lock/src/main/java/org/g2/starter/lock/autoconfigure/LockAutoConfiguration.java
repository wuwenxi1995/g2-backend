package org.g2.starter.lock.autoconfigure;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.config.ZookeeperLockConfigureProperties;
import org.g2.starter.lock.infra.listener.CuratorStartListener;
import org.g2.starter.lock.infra.service.impl.FairLockStrategy;
import org.g2.starter.lock.infra.service.impl.MultiLockStrategy;
import org.g2.starter.lock.infra.service.impl.ReadLockStrategy;
import org.g2.starter.lock.infra.service.impl.RedLockStrategy;
import org.g2.starter.lock.infra.service.impl.ReentrantLockStrategy;
import org.g2.starter.lock.infra.service.impl.WriteLockStrategy;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@Configuration
@EnableConfigurationProperties(value = {RedissonConfigureProperties.class, ZookeeperLockConfigureProperties.class})
@ComponentScan(basePackages = "org.g2.starter.lock.infra")
public class LockAutoConfiguration {

    @Bean(initMethod = "init")
    @ConditionalOnProperty(prefix = "g2.lock.redisson", value = "enable", havingValue = "true")
    public RedissonBuildFactory redissonBuildFactory(RedissonConfigureProperties properties) {
        return new RedissonBuildFactory(properties);
    }

    @Bean(
            name = "lockRedissonClient",
            destroyMethod = "shutdown"
    )
    @ConditionalOnBean(RedissonBuildFactory.class)
    public RedissonClient redissonClient(RedissonBuildFactory redissonBuildFactory) {
        return redissonBuildFactory.build();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(prefix = "g2.lock.zookeeper", value = "enable", havingValue = "true")
    public CuratorFramework curator(ZookeeperLockConfigureProperties properties) {
        return CuratorFrameworkFactory.builder()
                .connectString(properties.getConnectionUrl())
                .connectionTimeoutMs(properties.getConnectionTimeout())
                .sessionTimeoutMs(properties.getSessionTimeout())
                .retryPolicy(new ExponentialBackoffRetry(properties.getRetry().getBaseSleepTimeMs(), properties.getRetry().getMaxRetries()))
                .build();
    }

    @Bean
    @ConditionalOnBean(name = "curator")
    public CuratorStartListener curatorStartListener() {
        return new CuratorStartListener();
    }

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
