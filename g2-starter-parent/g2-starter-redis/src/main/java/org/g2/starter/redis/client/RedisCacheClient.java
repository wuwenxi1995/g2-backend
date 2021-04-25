package org.g2.starter.redis.client;

import org.g2.starter.redis.infra.customizer.CustomizerRedisListCommand;
import org.g2.starter.redis.infra.customizer.CustomizerRedisStreamCommand;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * redis cache 客户端
 *
 * @author wenxi.wu@hand-china.com on 2020/4/11 18:01
 */
public class RedisCacheClient extends RedisTemplate<String, String> implements
        CustomizerRedisListCommand<String, String>,
        CustomizerRedisStreamCommand<String, String> {

    private RedisConnection redisConnection;

    private RedisCacheClient() {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        this.setKeySerializer(redisSerializer);
        this.setValueSerializer(redisSerializer);
        this.setHashKeySerializer(redisSerializer);
        this.setHashValueSerializer(redisSerializer);
    }

    public RedisCacheClient(RedisConnectionFactory redisConnectionFactory) {
        this();
        this.setConnectionFactory(redisConnectionFactory);
        this.redisConnection = redisConnectionFactory.getConnection();
        this.afterPropertiesSet();
    }

    @Override
    @NonNull
    protected RedisConnection preProcessConnection(@NonNull RedisConnection connection,
                                                   boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }

    public RedisConnection getRedisConnection() {
        return redisConnection;
    }

    @Override
    public List<String> bLeftPop(int timeout, String key) {
        return get(redisConnection.bLPop(timeout, key.getBytes()));
    }

    @Override
    public List<String> bRightPop(int timeout, String key) {
        return get(redisConnection.bRPop(timeout, key.getBytes()));
    }

    private List<String> get(List<byte[]> data) {
        List<String> result = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (byte[] aByte : data) {
                result.add(new String(aByte));
            }
        }
        return result;
    }

    @Override
    public String bRightPopLeftPush(int timeout, String srcKey, String dstKey) {
        byte[] bytes = redisConnection.bRPopLPush(timeout, srcKey.getBytes(), dstKey.getBytes());
        if (bytes != null && bytes.length > 0) {
            return new String(bytes);
        }
        return null;
    }

    @Override
    public String xAdd(String key, Map<String, String> content) {
        Map<byte[], byte[]> data = new HashMap<>(content.size() << 1);
        for (Map.Entry<String, String> entry : content.entrySet()) {
            data.put(entry.getKey().getBytes(), entry.getValue().getBytes());
        }
        RecordId recordId = redisConnection.xAdd(key.getBytes(), data);
        return recordId == null ? null : recordId.getValue();
    }

    @Override
    public List<Map<String, String>> xRead() {
        // redisConnection.xRead()
        return null;
    }
}
