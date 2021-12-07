package org.g2.dynamic.redis.hepler.dynamic;

import org.g2.dynamic.redis.hepler.RedisHelper;
import org.g2.dynamic.redis.util.DatabaseThreadLocal;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

/**
 * 动态切换数据库
 *
 * @author wuwenxi 2021-12-07
 */
public class DynamicRedisHelper extends RedisHelper {

    private DynamicRedisTemplate<String, String> redisTemplate;

    public DynamicRedisHelper(DynamicRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(redisTemplate, "redisTemplate must not be null");
    }

    @Override
    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public void setCurrentDataBase(int index) {
        DatabaseThreadLocal.set(index);
    }

    @Override
    public void clearCurrentDataBase() {
        DatabaseThreadLocal.clear();
    }
}
