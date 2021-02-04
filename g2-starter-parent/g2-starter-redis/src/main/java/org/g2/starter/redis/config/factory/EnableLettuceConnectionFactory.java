package org.g2.starter.redis.config.factory;

import java.util.Set;

import org.g2.starter.redis.config.properties.RedisCacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.StringUtils;

/**
 * @author wenxi.wu@hand-china.com on 2020/4/15 11:41
 */
@Configuration
public class EnableLettuceConnectionFactory {

    @Bean
    public LettuceConnectionFactory customizerLettuceConnectionFactory(RedisCacheProperties redisCacheProperties) {
        LettuceClientConfiguration lettuceClientConfiguration = redisCacheProperties.buildLettuceClientConfiguration(redisCacheProperties.getPool().getPoolMaxIdle(),
                redisCacheProperties.getPool().getPoolMinIdle(),
                redisCacheProperties.getPool().getPoolMaxWaitTime(),
                redisCacheProperties.getPool().getTimeoutMillis());
        LettuceConnectionFactory lettuceConnectionFactory;
        // 哨兵模式
        if (redisCacheProperties.getSentinel() != null) {
            // org.springframework.util.StringUtils 以 ","分割字符串
            Set<String> sentinels = StringUtils.commaDelimitedListToSet(redisCacheProperties.getSentinel().getNodes());
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(redisCacheProperties.getSentinel().getMaster(), sentinels);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(redisCacheProperties.getPassword())) {
                redisSentinelConfiguration.setPassword(redisCacheProperties.getPassword());
            }
            redisSentinelConfiguration.setDatabase(redisCacheProperties.getDbIndex());
            lettuceConnectionFactory = new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
        }
        // 集群模式
        else if (redisCacheProperties.getCluster() != null) {
            Set<String> cluster = StringUtils.commaDelimitedListToSet(redisCacheProperties.getCluster().getNodes());
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(cluster);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(redisCacheProperties.getPassword())) {
                redisClusterConfiguration.setPassword(redisCacheProperties.getPassword());
            }
            if (redisCacheProperties.getCluster().getMaxRedirect() != null) {
                redisClusterConfiguration.setMaxRedirects(redisCacheProperties.getCluster().getMaxRedirect());
            }
            redisClusterConfiguration.setPassword(redisCacheProperties.getPassword());
            lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
        }
        // 单节点模式
        else {
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
            lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
        }
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

}
