package org.g2.message.listener.repository.impl;

import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.message.listener.repository.RedisQueueRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Arrays;
import java.util.List;

/**
 * @author wuwenxi 2022-12-07
 */
public class RedisQueueRepositoryImpl implements RedisQueueRepository {

    private static final String ACK = "%s:ack";
    private static final String ACK_EXPIRE = "%s:ack_expire";
    private static final DefaultRedisScript<String> REDIS_QUEUE_POP_SCRIP;
    private static final DefaultRedisScript<Void> REDIS_QUEUE_COMMIT_SCRIP;
    private static final DefaultRedisScript<Void> REDIS_QUEUE_ROLLBACK_SCRIP;

    private final DynamicRedisHelper dynamicRedisHelper;

    public RedisQueueRepositoryImpl(DynamicRedisHelper dynamicRedisHelper) {
        this.dynamicRedisHelper = dynamicRedisHelper;
    }

    @Override
    public void push(String key, String data) {
        push(0, key, data);
    }

    @Override
    public void push(int db, String key, String data) {
        dynamicRedisHelper.setCurrentDataBase(db);
        try {
            dynamicRedisHelper.lstRightPush(key, data);
        } finally {
            dynamicRedisHelper.clearCurrentDataBase();
        }
    }

    @Override
    public String pop(int db, String key, long now, long expire) {
        return execute(db, REDIS_QUEUE_POP_SCRIP, Arrays.asList(key, String.format(ACK, key), String.format(ACK_EXPIRE, key)), now, expire);
    }

    @Override
    public void commit(int db, String key, String data) {
        execute(db, REDIS_QUEUE_COMMIT_SCRIP, Arrays.asList(key, String.format(ACK, key), String.format(ACK_EXPIRE, key)), data);
    }

    @Override
    public void rollback(int db, String key, String data) {
        execute(db, REDIS_QUEUE_ROLLBACK_SCRIP, Arrays.asList(key, String.format(ACK, key), String.format(ACK_EXPIRE, key)), data);
    }

    private <T> T execute(int db, RedisScript<T> script, List<String> keys, Object... args) {
        dynamicRedisHelper.setCurrentDataBase(db);
        try {
            return dynamicRedisHelper.getRedisTemplate().execute(script, keys, args);
        } finally {
            dynamicRedisHelper.clearCurrentDataBase();
        }
    }

    static {
        REDIS_QUEUE_POP_SCRIP = new DefaultRedisScript<>();
        REDIS_QUEUE_POP_SCRIP.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/lua/redis_queue_pop.lua")));
        REDIS_QUEUE_POP_SCRIP.setResultType(String.class);

        REDIS_QUEUE_COMMIT_SCRIP = new DefaultRedisScript<>();
        REDIS_QUEUE_COMMIT_SCRIP.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/lua/redis_queue_commit.lua")));
        REDIS_QUEUE_COMMIT_SCRIP.setResultType(Void.class);

        REDIS_QUEUE_ROLLBACK_SCRIP = new DefaultRedisScript<>();
        REDIS_QUEUE_ROLLBACK_SCRIP.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/lua/redis_queue_rollback.lua")));
        REDIS_QUEUE_ROLLBACK_SCRIP.setResultType(Void.class);
    }
}
