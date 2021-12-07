package org.g2.dynamic.redis.hepler.sharding;

import org.g2.dynamic.redis.hepler.RedisHelper;
import org.g2.dynamic.redis.util.DatabaseThreadLocal;
import org.g2.dynamic.redis.util.RedisShardingTheadLocal;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

/**
 * redis分片，动态切换同库/不同库下的客户端
 *
 * @author wuwenxi 2021-12-07
 */
public class ShardingRedisHelper extends RedisHelper {

    private RedisTemplate<String, String> redisTemplate;

    public ShardingRedisHelper(ShardingRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(redisTemplate, "redisTemplate must not be null");
    }

    @Override
    public RedisTemplate<String, String> getRedisTemplate() {
        return this.redisTemplate;
    }

    @Override
    public void setCurrentDataBase(int index) {
        DatabaseThreadLocal.set(index);
    }

    @Override
    public void clearCurrentDataBase() {
        DatabaseThreadLocal.clear();
    }

    public void setShardingName(String lookupKey) {
        RedisShardingTheadLocal.set(lookupKey);
    }

    public void clearShardingName() {
        RedisShardingTheadLocal.clear();
    }
}
