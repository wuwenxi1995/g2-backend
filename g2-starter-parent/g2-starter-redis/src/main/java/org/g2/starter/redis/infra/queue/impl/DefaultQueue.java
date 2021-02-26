package org.g2.starter.redis.infra.queue.impl;

import java.util.List;

import org.g2.core.exception.CommonException;
import org.g2.core.helper.FastJsonHelper;
import org.g2.core.util.CollectionUtils;
import org.g2.starter.redis.client.RedisCacheClient;
import org.g2.starter.redis.infra.queue.IQueue;

/**
 * redis 发布/订阅队列默认实现
 *
 * @author wenxi.wu@hand-china.com 2020-11-06
 */
public class DefaultQueue implements IQueue {

    private final RedisCacheClient redisCacheClient;

    public DefaultQueue(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void push(String key, Object value) {
        redisCacheClient.opsForList().rightPush(key, FastJsonHelper.objectConvertString(value));
    }

    @Override
    public void push(String key, String value) {
        redisCacheClient.opsForList().rightPush(key, value);
    }

    @Override
    public String pull(String key) {
        return redisCacheClient.opsForList().leftPop(key);
    }

    @Override
    public List<String> pullAll(String key) {
        redisCacheClient.multi();
        List<String> range = redisCacheClient.opsForList().range(key, 0, -1);
        if (CollectionUtils.isNotEmpty(range)) {
            redisCacheClient.opsForList().remove(key, 1, range);
        }
        redisCacheClient.exec();
        return range;
    }

    @Override
    public List<String> pullAll(String key, int end) {
        redisCacheClient.multi();
        List<String> range = redisCacheClient.opsForList().range(key, 0, end);
        if (CollectionUtils.isNotEmpty(range)) {
            redisCacheClient.opsForList().remove(key, 0, range);
        }
        redisCacheClient.exec();
        return range;
    }

    @Override
    public List<String> pullAll(String key, int start, int end) {
        if (start < end) {
            throw new CommonException("[start] needs to be greater than or equal to [end]");
        }
        redisCacheClient.multi();
        List<String> range = redisCacheClient.opsForList().range(key, start, end);
        if (CollectionUtils.isNotEmpty(range)) {
            redisCacheClient.opsForList().remove(key, 0, range);
        }
        redisCacheClient.exec();
        return range;
    }
}
