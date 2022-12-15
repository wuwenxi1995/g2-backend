package org.g2.message.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.message.repository.RedisQueueRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author wuwenxi 2022-12-07
 */
public class RedisQueueRepositoryImpl implements RedisQueueRepository {

    private static final String ACK = "%s:ack";
    private static final String RETRY_TIMES = "message:queue:hash:retry_times";
    private static final String RETRY_ERROR = "message:queue:retry_error:%s";
    private static final DefaultRedisScript<Void> REDIS_QUEUE_ROLLBACK_SCRIP;
    private static final DefaultRedisScript<Boolean> REDIS_QUEUE_ACK_TIMEOUT_SCRIP;

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
        operation(db, () -> dynamicRedisHelper.lstRightPush(key, data));
    }

    @Override
    public String poll(int db, String key, Duration pollTimeout) {
        return operation(db, () -> {
            String data = dynamicRedisHelper.lstLeftPop(key, pollTimeout.toMillis(), TimeUnit.MILLISECONDS);
            if (StringUtils.isNotEmpty(data)) {
                dynamicRedisHelper.zSetAdd(String.format(ACK, key), data, System.currentTimeMillis());
            }
            return data;
        });
    }

    @Override
    public void commit(int db, String key, String data) {
        operation(db, () -> dynamicRedisHelper.zSetRemove(String.format(ACK, key), data));
    }

    @Override
    public void rollback(int db, String key, String data, int retry) {
        String times;
        if (retry < 0 ||
                (StringUtils.isNotEmpty(times = operation(db, () -> dynamicRedisHelper.hshGet(RETRY_TIMES, key)))
                        && Integer.parseInt(times) > retry)) {
            dynamicRedisHelper.hshDelete(RETRY_TIMES, key);
            dynamicRedisHelper.lstRightPush(String.format(RETRY_ERROR, key), data);
            return;
        }
        execute(db, REDIS_QUEUE_ROLLBACK_SCRIP, Arrays.asList(key, String.format(ACK, key), RETRY_TIMES), data);
    }

    @Override
    public boolean ackTimeout(int db, String key, Duration ackTimeout) {
        return execute(db, REDIS_QUEUE_ACK_TIMEOUT_SCRIP, Arrays.asList(key, String.format(ACK, key)), System.currentTimeMillis() - ackTimeout.toMillis());
    }

    private <T> T execute(int db, RedisScript<T> script, List<String> keys, Object... args) {
        return operation(db, () -> dynamicRedisHelper.getRedisTemplate().execute(script, keys, args));
    }

    private <T> T operation(int db, Supplier<T> supplier) {
        dynamicRedisHelper.setCurrentDataBase(db);
        try {
            return supplier.get();
        } finally {
            dynamicRedisHelper.clearCurrentDataBase();
        }
    }

    static {

        REDIS_QUEUE_ROLLBACK_SCRIP = new DefaultRedisScript<>();
        REDIS_QUEUE_ROLLBACK_SCRIP.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/lua/redis_queue_rollback.lua")));
        REDIS_QUEUE_ROLLBACK_SCRIP.setResultType(Void.class);


        REDIS_QUEUE_ACK_TIMEOUT_SCRIP = new DefaultRedisScript<>();
        REDIS_QUEUE_ACK_TIMEOUT_SCRIP.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/lua/redis_queue_ack_timeout.lua")));
        REDIS_QUEUE_ACK_TIMEOUT_SCRIP.setResultType(Boolean.class);
    }
}
