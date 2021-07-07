package org.g2.starter.redis.config.handler.impl;

import org.g2.core.chain.ChainHandler;
import org.g2.starter.redis.config.handler.RedisConfiguration;
import org.g2.starter.redis.config.properties.RedisCacheProperties;
import org.g2.starter.redis.infra.enums.RedisServerPattern;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
@Component
public class SentinelConfiguration extends RedisConfiguration {

    public SentinelConfiguration(RedisCacheProperties redisCacheProperties) {
        super(redisCacheProperties);
    }

    @Override
    public Object invoke(ChainHandler chainHandler) throws Exception {
        if (!RedisServerPattern.SENTINEL.getPattern().equals(redisCacheProperties.getPattern())) {
            return chainHandler.proceed();
        }
        // org.springframework.util.StringUtils 以 ","分割字符串
        Set<String> sentinels = StringUtils.commaDelimitedListToSet(redisCacheProperties.getSentinel().getNodes());
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(redisCacheProperties.getSentinel().getMaster(), sentinels);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(redisCacheProperties.getPassword())) {
            redisSentinelConfiguration.setPassword(redisCacheProperties.getPassword());
        }
        redisSentinelConfiguration.setDatabase(redisCacheProperties.getDbIndex());
        return new LettuceConnectionFactory(redisSentinelConfiguration, buildLettuceClientConfiguration());
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
