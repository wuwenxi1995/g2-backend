package org.g2.starter.zk.autoconfigure;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.g2.starter.zk.lock.config.ZookeeperLockConfigureProperties;
import org.g2.starter.zk.lock.listener.CuratorStartListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2021-10-11
 */
@Configuration
@EnableConfigurationProperties(value = ZookeeperLockConfigureProperties.class)
public class ZkAutoConfiguration {

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

}
