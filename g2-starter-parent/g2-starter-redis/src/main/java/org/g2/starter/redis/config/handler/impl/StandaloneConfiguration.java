package org.g2.starter.redis.config.handler.impl;

import org.g2.core.handler.InvocationHandler;
import org.g2.starter.redis.config.handler.RedisConfiguration;
import org.g2.starter.redis.config.properties.RedisCacheProperties;
import org.g2.starter.redis.infra.enums.RedisServerPattern;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
@Component
public class StandaloneConfiguration extends RedisConfiguration {

    public StandaloneConfiguration(RedisCacheProperties redisCacheProperties) {
        super(redisCacheProperties);
    }

    @Override
    public Object invoke(InvocationHandler invocationHandler) throws Exception {
        if (!RedisServerPattern.STANDALONE.getPattern().equals(redisCacheProperties.getPattern())) {
            return invocationHandler.proceed();
        }
        // 创建单节点
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        // 主机地址
        redisStandaloneConfiguration.setHostName(redisCacheProperties.getHost());
        // 端口
        redisStandaloneConfiguration.setPort(redisCacheProperties.getPort());
        // 密码
        redisStandaloneConfiguration.setPassword(redisCacheProperties.getPassword());
        // db库
        redisStandaloneConfiguration.setDatabase(redisCacheProperties.getDbIndex());
        // 创建连接工厂
        return new LettuceConnectionFactory(redisStandaloneConfiguration, buildLettuceClientConfiguration());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
