package org.g2.inv.core.infra.repository.impl;

import com.alibaba.fastjson.JSONObject;
import org.g2.dynamic.redis.hepler.sharding.ShardingRedisHelper;
import org.g2.inv.core.domain.repository.StockRedisRepository;
import org.slf4j.Logger;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author wuwenxi 2022-04-15
 */
public class BaseStockRedisRepository implements StockRedisRepository {

    private final ShardingRedisHelper shardingRedisHelper;
    private final Logger log;

    BaseStockRedisRepository(ShardingRedisHelper shardingRedisHelper, Logger logger) {
        this.shardingRedisHelper = shardingRedisHelper;
        this.log = logger;
    }

    @Override
    public <T> T operation(String posCode, Supplier<T> supplier) {
        shardingRedisHelper.setShardingName(posCode);
        try {
            T result = supplier.get();
            log.info("operation stock redis result : {}", JSONObject.toJSONString(result));
            return result;
        } finally {
            shardingRedisHelper.clearShardingName();
        }
    }

    @Override
    public <T> T execute(RedisScript<T> redisScript, List<String> key, Object... params) {
        return shardingRedisHelper.getRedisTemplate().execute(redisScript, key, params);
    }
}
