package org.g2.starter.redisson.autoconfigure;

import org.g2.core.exception.CommonException;
import org.g2.starter.redisson.autoconfigure.responsibility.ServerConfig;
import org.g2.starter.redisson.autoconfigure.responsibility.impl.ClusterServerConfig;
import org.g2.starter.redisson.autoconfigure.responsibility.impl.MasterSlaveServerConfig;
import org.g2.starter.redisson.autoconfigure.responsibility.impl.ReplicatedServerConfig;
import org.g2.starter.redisson.autoconfigure.responsibility.impl.SentinelServerConfig;
import org.g2.starter.redisson.autoconfigure.responsibility.impl.SingleServerConfig;
import org.g2.starter.redisson.config.LockConfigureProperties;
import org.g2.starter.redisson.infra.service.impl.FairLockStrategy;
import org.g2.starter.redisson.infra.service.impl.MultiLockStrategy;
import org.g2.starter.redisson.infra.service.impl.ReadLockStrategy;
import org.g2.starter.redisson.infra.service.impl.RedLockStrategy;
import org.g2.starter.redisson.infra.service.impl.ReentrantLockStrategy;
import org.g2.starter.redisson.infra.service.impl.WriteLockStrategy;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@Configuration
@EnableConfigurationProperties(value = {LockConfigureProperties.class})
@ComponentScan(basePackages = "org.g2.starter.redisson")
public class RedissonAutoConfiguration {

    @Bean(
            name = "lockRedissonClient",
            destroyMethod = "shutdown"
    )
    public RedissonClient redissonClient(LockConfigureProperties properties) throws Exception {
        Config config = new Config();
        config.setThreads(properties.getThreads());
        config.setNettyThreads(properties.getNettyThreads());
        config.setKeepPubSubOrder(properties.isKeepPubSubOrder());
        config.setLockWatchdogTimeout(properties.getLockWatchdogTimeout());
        config.setUseScriptCache(properties.isUseScriptCache());
        // 生成其他配置信息
        new RedissonClientAutoConfigureHandler(config, properties).proceed();
        return Redisson.create(config);
    }

    @Bean
    @Scope("prototype")
    public FairLockStrategy fairLockStrategy() {
        return new FairLockStrategy();
    }

    @Bean
    @Scope("prototype")
    public ReentrantLockStrategy reentrantLockStrategy() {
        return new ReentrantLockStrategy();
    }

    @Bean
    @Scope("prototype")
    public MultiLockStrategy multiLockStrategy() {
        return new MultiLockStrategy();
    }

    @Bean
    @Scope("prototype")
    public ReadLockStrategy readLockStrategy() {
        return new ReadLockStrategy();
    }

    @Bean
    @Scope("prototype")
    public RedLockStrategy redLockStrategy() {
        return new RedLockStrategy();
    }

    @Bean
    @Scope("prototype")
    public WriteLockStrategy writeLockStrategy() {
        return new WriteLockStrategy();
    }

    /**
     * 责任链 -- 构建redisson客户端其他配置信息
     */
    public static class RedissonClientAutoConfigureHandler {
        private LockConfigureProperties properties;
        private Config config;

        private int currentHandlerIndex = -1;
        private List<ServerConfig> serverConfigList;

        private RedissonClientAutoConfigureHandler(Config config, LockConfigureProperties properties) {
            this.properties = properties;
            this.config = config;
            init();
        }

        private void init() {
            serverConfigList = new ArrayList<>();
            serverConfigList.add(new SingleServerConfig(config, properties));
            serverConfigList.add(new MasterSlaveServerConfig(config, properties));
            serverConfigList.add(new SentinelServerConfig(config, properties));
            serverConfigList.add(new ClusterServerConfig(config, properties));
            serverConfigList.add(new ReplicatedServerConfig(config, properties));
        }

        public Object proceed() throws URISyntaxException {
            if (currentHandlerIndex == serverConfigList.size() - 1) {
                throw new CommonException(String.format("redisson pattern [%s] is not found", properties.getPattern()));
            }
            ServerConfig serverConfig = serverConfigList.get(++currentHandlerIndex);
            return serverConfig.invoke(this);
        }
    }
}
