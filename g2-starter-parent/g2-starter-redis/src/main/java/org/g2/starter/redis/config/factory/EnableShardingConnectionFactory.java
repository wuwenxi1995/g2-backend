package org.g2.starter.redis.config.factory;

import java.util.Map;
import java.util.Set;

import org.g2.starter.redis.client.RedisShardingClient;
import org.g2.starter.redis.config.properties.RedisCacheProperties;
import org.g2.starter.redis.config.properties.RedisShardingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

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
        LettuceClientConfiguration lettuceClientConfiguration = redisShardingProperties.buildLettuceClientConfiguration();
        for (Map.Entry<String, RedisShardingProperties.RedisShardingConnection> shardingConnection : instances.entrySet()) {
            RedisShardingProperties.RedisShardingConnection instance = shardingConnection.getValue();

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

            lettuceConnectionFactory.afterPropertiesSet();
            redisShardingClient.addRedisConnectionFactory(shardingConnection.getKey(), lettuceConnectionFactory);

            StringRedisTemplate redisTemplate = new StringRedisTemplate(lettuceConnectionFactory);
            redisShardingClient.addRedisTemplate(shardingConnection.getKey(), redisTemplate);

        }
        return redisShardingClient;
    }
}
