package org.g2.starter.redis.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
public class RedisShardingClient {

    private Map<String, RedisConnectionFactory> redisConnectionFactories;
    private Map<String, StringRedisTemplate> redisTemplates;

    public RedisShardingClient() {
        redisConnectionFactories = new ConcurrentHashMap<>();
        redisTemplates = new ConcurrentHashMap<>();
    }

    public void addRedisConnectionFactory(String instanceName, RedisConnectionFactory redisConnectionFactory) {
        redisConnectionFactories.put(instanceName, redisConnectionFactory);
    }

    public void addRedisTemplate(String instanceName, StringRedisTemplate redisTemplate) {
        redisTemplates.put(instanceName, redisTemplate);
    }

    public RedisConnectionFactory getRedisConnectionFactory(String instanceName) {
        return redisConnectionFactories.get(instanceName);
    }

    public StringRedisTemplate getStringRedisTemplate(String instanceName) {
        return redisTemplates.get(instanceName);
    }
}
