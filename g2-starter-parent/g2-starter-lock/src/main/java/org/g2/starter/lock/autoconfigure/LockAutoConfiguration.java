package org.g2.starter.lock.autoconfigure;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.g2.core.exception.CommonException;
import org.g2.core.handler.MethodInvocationHandler;
import org.g2.core.handler.impl.ChainInvocationHandler;
import org.g2.starter.lock.autoconfigure.responsibility.impl.ClusterServerConfig;
import org.g2.starter.lock.autoconfigure.responsibility.impl.MasterSlaveServerConfig;
import org.g2.starter.lock.autoconfigure.responsibility.impl.ReplicatedServerConfig;
import org.g2.starter.lock.autoconfigure.responsibility.impl.SentinelServerConfig;
import org.g2.starter.lock.autoconfigure.responsibility.impl.SingleServerConfig;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.config.ZookeeperLockConfigureProperties;
import org.g2.starter.lock.infra.constants.LockConstants;
import org.g2.starter.lock.infra.listener.CuratorStartListener;
import org.g2.starter.lock.infra.service.impl.FairLockStrategy;
import org.g2.starter.lock.infra.service.impl.MultiLockStrategy;
import org.g2.starter.lock.infra.service.impl.ReadLockStrategy;
import org.g2.starter.lock.infra.service.impl.RedLockStrategy;
import org.g2.starter.lock.infra.service.impl.ReentrantLockStrategy;
import org.g2.starter.lock.infra.service.impl.WriteLockStrategy;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@Configuration
@EnableConfigurationProperties(value = {RedissonConfigureProperties.class, ZookeeperLockConfigureProperties.class})
@ComponentScan(basePackages = "org.g2.starter.lock")
public class LockAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LockAutoConfiguration.class);

    @Bean(
            name = "lockRedissonClient",
            destroyMethod = "shutdown"
    )
    @ConditionalOnProperty(prefix = "g2.lock.redisson", value = "enable", havingValue = "true")
    public RedissonClient redissonClient(RedissonConfigureProperties properties) {
        Config config = new Config();
        config.setTransportMode(LockConstants.TransportMode.getTransportMode(properties.getTransportMode()));
        config.setThreads(properties.getThreads());
        config.setNettyThreads(properties.getNettyThreads());
        config.setKeepPubSubOrder(properties.isKeepPubSubOrder());
        config.setLockWatchdogTimeout(properties.getLockWatchdogTimeout());
        config.setUseScriptCache(properties.isUseScriptCache());
        // 生成其他配置信息
        new RedissonClientAutoConfigureHandler(initChain(config, properties)).proceed();
        return Redisson.create(config);
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
    public CuratorStartListener curatorStartListener(){
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

    /**
     * 责任链 -- 构建redisson客户端其他配置信息
     */
    private static class RedissonClientAutoConfigureHandler extends ChainInvocationHandler {

        private RedissonClientAutoConfigureHandler(List<? extends MethodInvocationHandler> methodInvocationHandlerList) {
            super(methodInvocationHandlerList);
        }

        @Override
        public Object proceed() {
            try {
                return super.proceed();
            } catch (Exception e) {
                throw new CommonException(e);
            }
        }

        @Override
        protected Object invoke() {
            throw new CommonException("No suitable handler found");
        }
    }

    private List<MethodInvocationHandler> initChain(Config config, RedissonConfigureProperties properties) {
        List<MethodInvocationHandler> methodInvocationHandlerList = new ArrayList<>();
        methodInvocationHandlerList.add(new SingleServerConfig(config, properties));
        methodInvocationHandlerList.add(new MasterSlaveServerConfig(config, properties));
        methodInvocationHandlerList.add(new SentinelServerConfig(config, properties));
        methodInvocationHandlerList.add(new ClusterServerConfig(config, properties));
        methodInvocationHandlerList.add(new ReplicatedServerConfig(config, properties));
        return methodInvocationHandlerList;
    }
}
