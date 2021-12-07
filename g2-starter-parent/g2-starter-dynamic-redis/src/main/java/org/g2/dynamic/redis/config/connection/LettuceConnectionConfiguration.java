package org.g2.dynamic.redis.config.connection;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.g2.dynamic.redis.RedisConnectionConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author wuwenxi 2021-12-07
 */
public class LettuceConnectionConfiguration extends RedisConnectionConfiguration {

    private final RedisProperties properties;
    private final List<LettuceClientConfigurationBuilderCustomizer> builderCustomizers;
    private final ClientResources clientResources;

    public LettuceConnectionConfiguration(RedisProperties properties,
                                          ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
                                          ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
                                          ObjectProvider<List<LettuceClientConfigurationBuilderCustomizer>> builderCustomizers, int database) {
        super(properties, sentinelConfiguration, clusterConfiguration, database);
        this.properties = properties;
        this.builderCustomizers = builderCustomizers.getIfAvailable(Collections::emptyList);
        this.clientResources = DefaultClientResources.create();
    }

    @Override
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(
                clientResources, this.properties.getLettuce().getPool());
        return createLettuceConnectionFactory(clientConfig);
    }

    private RedisConnectionFactory createLettuceConnectionFactory(LettuceClientConfiguration clientConfiguration) {
        LettuceConnectionFactory lettuceConnectionFactory;
        if (getSentinelConfig() != null) {
            lettuceConnectionFactory = new LettuceConnectionFactory(getSentinelConfig(), clientConfiguration);
        } else if (getClusterConfiguration() != null) {
            lettuceConnectionFactory = new LettuceConnectionFactory(getClusterConfiguration(), clientConfiguration);
        } else {
            lettuceConnectionFactory = new LettuceConnectionFactory(getStandaloneConfig(), clientConfiguration);
        }

        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private LettuceClientConfiguration getLettuceClientConfiguration(ClientResources clientResources, RedisProperties.Pool pool) {
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder;
        if (pool == null) {
            builder = LettuceClientConfiguration.builder();
        } else {
            builder = LettucePoolingClientConfiguration.builder().poolConfig(getPoolConfig(pool));
        }
        if (this.properties.isSsl()) {
            builder.useSsl();
        }
        if (this.properties.getTimeout() != null) {
            builder.commandTimeout(this.properties.getTimeout());
        }
        if (this.properties.getLettuce() != null) {
            RedisProperties.Lettuce lettuce = this.properties.getLettuce();
            if (lettuce.getShutdownTimeout() != null
                    && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(
                        this.properties.getLettuce().getShutdownTimeout());
            }
        }
        if (StringUtils.hasText(this.properties.getUrl())) {
            Connection connection = parseUrl(this.properties.getUrl());
            if (connection.isUseSsl()) {
                builder.useSsl();
            }
        }
        builder.clientResources(clientResources);
        for (LettuceClientConfigurationBuilderCustomizer customizer : this.builderCustomizers) {
            customizer.customize(builder);
        }
        return builder.build();
    }

    private GenericObjectPoolConfig getPoolConfig(RedisProperties.Pool pool) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        if (pool.getMaxWait() != null) {
            config.setMaxWaitMillis(pool.getMaxWait().toMillis());
        }
        return config;
    }
}
