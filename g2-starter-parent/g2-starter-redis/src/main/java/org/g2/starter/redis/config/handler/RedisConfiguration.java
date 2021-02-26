package org.g2.starter.redis.config.handler;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.g2.core.handler.InvocationHandler;
import org.g2.core.handler.MethodInvocationHandler;
import org.g2.starter.redis.config.properties.RedisCacheProperties;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
public abstract class RedisConfiguration implements MethodInvocationHandler<LettuceConnectionFactory> {

    protected RedisCacheProperties redisCacheProperties;

    protected RedisConfiguration(RedisCacheProperties redisCacheProperties) {
        this.redisCacheProperties = redisCacheProperties;
    }

    protected LettuceClientConfiguration buildLettuceClientConfiguration() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        RedisCacheProperties.Pool pool = redisCacheProperties.getPool();
        genericObjectPoolConfig.setMaxIdle(pool.getPoolMaxIdle());
        genericObjectPoolConfig.setMinIdle(pool.getPoolMinIdle());
        genericObjectPoolConfig.setMaxTotal(pool.getPoolMaxIdle());
        genericObjectPoolConfig.setMaxWaitMillis(pool.getPoolMaxWaitTime());
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .commandTimeout(Duration.ofMillis(pool.getTimeoutMillis()))
                .build();
    }
}
