package org.g2.starter.dynamic.redis.config.connection;

import org.g2.starter.dynamic.redis.RedisConnectionConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * @author wuwenxi 2021-12-07
 */
public class JedisConnectionConfiguration extends RedisConnectionConfiguration {

    private final RedisProperties properties;
    private final List<JedisClientConfigurationBuilderCustomizer> builderCustomizers;

    public JedisConnectionConfiguration(RedisProperties properties,
                                        ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
                                        ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
                                        ObjectProvider<List<JedisClientConfigurationBuilderCustomizer>> builderCustomizers) {
        super(properties, sentinelConfiguration, clusterConfiguration);
        this.properties = properties;
        this.builderCustomizers = builderCustomizers.getIfAvailable(Collections::emptyList);
    }

    @Override
    public RedisConnectionFactory redisConnectionFactory() {
        JedisClientConfiguration clientConfiguration = getJedisClientConfiguration();
        if (getSentinelConfig() != null) {
            return new JedisConnectionFactory(getSentinelConfig(), clientConfiguration);
        }
        if (getClusterConfiguration() != null) {
            return new JedisConnectionFactory(getClusterConfiguration(), clientConfiguration);
        }
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(getStandaloneConfig(), clientConfiguration);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    private JedisClientConfiguration getJedisClientConfiguration() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        if (this.properties.isSsl()) {
            builder.useSsl();
        }
        if (this.properties.getTimeout() != null) {
            Duration timeout = this.properties.getTimeout();
            builder.readTimeout(timeout).connectTimeout(timeout);
        }
        RedisProperties.Pool pool = this.properties.getJedis().getPool();
        if (pool != null) {
            builder.usePooling().poolConfig(jedisPoolConfig(pool));
        }
        if (StringUtils.hasText(this.properties.getUrl())) {
            Connection connection = parseUrl(this.properties.getUrl());
            if (connection.isUseSsl()) {
                builder.useSsl();
            }
        }
        for (JedisClientConfigurationBuilderCustomizer customizer : this.builderCustomizers) {
            customizer.customize(builder);
        }
        return builder.build();
    }

    private JedisPoolConfig jedisPoolConfig(RedisProperties.Pool pool) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        if (pool.getMaxWait() != null) {
            config.setMaxWaitMillis(pool.getMaxWait().toMillis());
        }
        return config;
    }
}
