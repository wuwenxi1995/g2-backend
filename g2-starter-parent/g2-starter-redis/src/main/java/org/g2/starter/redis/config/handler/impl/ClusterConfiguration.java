package org.g2.starter.redis.config.handler.impl;

import org.g2.core.handler.InvocationHandler;
import org.g2.starter.redis.config.handler.RedisConfiguration;
import org.g2.starter.redis.config.properties.RedisCacheProperties;
import org.g2.starter.redis.infra.enums.RedisServerPattern;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
@Component
public class ClusterConfiguration extends RedisConfiguration {

    public ClusterConfiguration(RedisCacheProperties redisCacheProperties) {
        super(redisCacheProperties);
    }

    @Override
    public Object invoke(InvocationHandler invocationHandler) throws Exception {
        if (!RedisServerPattern.CLUSTER.getPattern().equals(redisCacheProperties.getPattern())) {
            return invocationHandler.proceed();
        }
        Set<String> cluster = StringUtils.commaDelimitedListToSet(redisCacheProperties.getCluster().getNodes());
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(cluster);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(redisCacheProperties.getPassword())) {
            redisClusterConfiguration.setPassword(redisCacheProperties.getPassword());
        }
        if (redisCacheProperties.getCluster().getMaxRedirect() != null) {
            redisClusterConfiguration.setMaxRedirects(redisCacheProperties.getCluster().getMaxRedirect());
        }
        return new LettuceConnectionFactory(redisClusterConfiguration, buildLettuceClientConfiguration());
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
