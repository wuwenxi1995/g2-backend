package org.g2.starter.redis.config.properties;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
public class RedisProperties {
    /**
     * lettuce连接池配置
     *
     * @return lettuceClientConfiguration
     */
    public LettuceClientConfiguration buildLettuceClientConfiguration(int poolMaxIdle, int poolMinIdle, long poolMaxWaitTime, long timeoutMillis) {
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
