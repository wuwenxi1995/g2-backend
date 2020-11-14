package org.g2.starter.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@Configuration
public class RedissonAutoConfiguration {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setTransportMode(TransportMode.EPOLL);

        ClusterServersConfig clusterServersConfig = config.useClusterServers();

        return Redisson.create(config);
    }
}
