package org.g2.starter.redis.client;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;

/**
 * redis cache 客户端
 *
 * @author wenxi.wu@hand-china.com on 2020/4/11 18:01
 */
public class RedisCacheClient extends RedisTemplate<String, String>{

    public static final String BEAN_NAME = "redisCacheClient";

    private final RedisSerializer<String> redisSerializer = getStringSerializer();

    private RedisCacheClient() {
        this.setKeySerializer(redisSerializer);
        this.setValueSerializer(redisSerializer);
        this.setHashKeySerializer(redisSerializer);
        this.setHashValueSerializer(redisSerializer);
    }

    public RedisCacheClient(RedisConnectionFactory redisConnectionFactory) {
        this();
        this.setConnectionFactory(redisConnectionFactory);
        this.afterPropertiesSet();
    }

    @Override
    @NonNull
    protected RedisConnection preProcessConnection(@NonNull RedisConnection connection,
                                                   boolean existingConnection) {
        return new DefaultStringRedisConnection(connection, redisSerializer);
    }
}
