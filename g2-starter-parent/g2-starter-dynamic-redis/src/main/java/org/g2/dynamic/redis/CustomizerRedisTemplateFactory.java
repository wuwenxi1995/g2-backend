package org.g2.dynamic.redis;

import org.g2.dynamic.redis.config.connection.JedisConnectionConfiguration;
import org.g2.dynamic.redis.config.connection.LettuceConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import java.util.List;

/**
 * redis客户端创建工厂
 *
 * @author wuwenxi 2021-12-07
 */
public class CustomizerRedisTemplateFactory<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CustomizerRedisTemplateFactory.class);
    private static final String REDIS_CLIENT_LETTUCE = "lettuce";
    private static final String REDIS_CLIENT_JEDIS = "jedis";

    private RedisProperties properties;
    private ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration;
    private ObjectProvider<RedisClusterConfiguration> clusterConfiguration;
    private ObjectProvider<List<JedisClientConfigurationBuilderCustomizer>> jedisBuilderCustomizers;
    private ObjectProvider<List<LettuceClientConfigurationBuilderCustomizer>> lettuceBuilderCustomizers;

    private RedisConnectionConfiguration redisConnectionConfiguration;

    public CustomizerRedisTemplateFactory(RedisProperties properties,
                                          ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
                                          ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
                                          ObjectProvider<List<JedisClientConfigurationBuilderCustomizer>> jedisBuilderCustomizers,
                                          ObjectProvider<List<LettuceClientConfigurationBuilderCustomizer>> lettuceBuilderCustomizers) {
        this.properties = properties;
        this.sentinelConfiguration = sentinelConfiguration;
        this.clusterConfiguration = clusterConfiguration;
        this.jedisBuilderCustomizers = jedisBuilderCustomizers;
        this.lettuceBuilderCustomizers = lettuceBuilderCustomizers;
    }

    public RedisTemplate<K, V> createRedisTemplate(int database) {
        Assert.notNull(redisConnectionConfiguration, "redisConnectionConfigure is null");
        RedisConnectionFactory redisConnectionFactory = redisConnectionConfiguration.setDatabase(database).redisConnectionFactory();
        return createRedisTemplate(redisConnectionFactory);
    }

    private RedisTemplate<K, V> createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setStringSerializer(stringRedisSerializer);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private void init() {
        switch (getRedisClientType()) {
            case REDIS_CLIENT_LETTUCE:
                redisConnectionConfiguration = new LettuceConnectionConfiguration(properties, sentinelConfiguration,
                        clusterConfiguration, lettuceBuilderCustomizers);
                break;
            case REDIS_CLIENT_JEDIS:
                redisConnectionConfiguration = new JedisConnectionConfiguration(properties, sentinelConfiguration,
                        clusterConfiguration, jedisBuilderCustomizers);
                break;
            default:
                //
        }
    }

    private String getRedisClientType() {
        try {
            Class.forName("io.lettuce.core.RedisClient");
            return REDIS_CLIENT_LETTUCE;
        } catch (ClassNotFoundException e) {
            log.error("Not Lettuce redis client");
        }
        try {
            Class.forName("redis.clients.jedis.Jedis");
            return REDIS_CLIENT_JEDIS;
        } catch (ClassNotFoundException e) {
            log.error("Not Jedis redis client");
        }
        throw new RuntimeException("redis client not found.");
    }
}
