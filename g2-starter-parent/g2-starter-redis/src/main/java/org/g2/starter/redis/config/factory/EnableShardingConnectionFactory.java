package org.g2.starter.redis.config.factory;

import java.time.Duration;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.g2.starter.redis.client.RedisShardingClient;
import org.g2.starter.redis.config.properties.RedisShardingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@Configuration
public class EnableShardingConnectionFactory {

    @Bean
    @ConditionalOnProperty(
            prefix = "g2.redis.shard",
            name = "enable",
            havingValue = "true"
    )
    public RedisShardingClient redisShardingClient(RedisShardingProperties redisShardingProperties) {
        RedisShardingClient redisShardingClient = new RedisShardingClient();
        Map<String, RedisShardingProperties.RedisShardingConnection> instances = redisShardingProperties.getInstances();
        if (instances.isEmpty()) {
            return redisShardingClient;
        }
        for (Map.Entry<String, RedisShardingProperties.RedisShardingConnection> shardingConnection : instances.entrySet()) {
            RedisShardingProperties.RedisShardingConnection instance = shardingConnection.getValue();
            LettuceClientConfiguration lettuceClientConfiguration = buildLettuceClientConfiguration(
                    redisShardingProperties.getPool().getPoolMaxIdle(),
                    redisShardingProperties.getPool().getPoolMinIdle(),
                    redisShardingProperties.getPool().getPoolMaxWaitTime(),
                    instance.getTimeoutMillis());

            LettuceConnectionFactory lettuceConnectionFactory;
            // 创建单节点
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            // 主机地址
            redisStandaloneConfiguration.setHostName(instance.getHost());
            // 端口
            redisStandaloneConfiguration.setPort(instance.getPort());
            // 密码
            redisStandaloneConfiguration.setPassword(instance.getPassword());
            // db库
            redisStandaloneConfiguration.setDatabase(instance.getDbIndex());
            // 创建连接工厂
            lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
        }
        return redisShardingClient;
    }

    private LettuceClientConfiguration buildLettuceClientConfiguration(int poolMaxIdle, int poolMinIdle, long poolMaxWaitTime, long timeoutMillis) {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(poolMaxIdle);
        genericObjectPoolConfig.setMinIdle(poolMinIdle);
        genericObjectPoolConfig.setMaxTotal(poolMaxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(poolMaxWaitTime);
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .commandTimeout(Duration.ofMillis(timeoutMillis))
                .build();
    }
}
