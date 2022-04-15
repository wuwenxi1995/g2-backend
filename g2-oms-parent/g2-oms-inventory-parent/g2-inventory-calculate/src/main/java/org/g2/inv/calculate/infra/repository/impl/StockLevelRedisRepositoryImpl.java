package org.g2.inv.calculate.infra.repository.impl;

import org.g2.dynamic.redis.hepler.sharding.ShardingRedisHelper;
import org.g2.inv.calculate.domain.repository.StockLevelRedisRepository;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.infra.constant.InvCoreConstant;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wuwenxi 2022-04-13
 */
@Repository
public class StockLevelRedisRepositoryImpl implements StockLevelRedisRepository {

    private static final ResourceScriptSource SYNC_STOCK_LEVEL = new ResourceScriptSource(new ClassPathResource("/script/lua/sync-stock-level.lua"));

    private final ShardingRedisHelper shardingRedisHelper;

    public StockLevelRedisRepositoryImpl(ShardingRedisHelper shardingRedisHelper) {
        this.shardingRedisHelper = shardingRedisHelper;
    }

    @Override
    public void sync(StockLevel stockLevel) {
        Object[] param = new Object[5];
        param[0] = String.valueOf(stockLevel.getOnHand() - stockLevel.getReserved());
        param[1] = String.format(InvCoreConstant.RedisKeyFormat.STOCK_LEVEL_KEY, stockLevel.getPosCode(), stockLevel.getSkuCode());
        param[2] = InvCoreConstant.InvStockKey.HASH_STOCK_LEVEL;
        param[3] = InvCoreConstant.InvStockKey.HASH_EXTEND_STOCK_RESERVED;
        param[4] = InvCoreConstant.InvStockKey.HASH_EXTEND_STOCK_ATS;
        shardingRedisHelper.setShardingName(stockLevel.getPosCode());
        try {
            execute(SYNC_STOCK_LEVEL, null, Boolean.class, param);
        } finally {
            shardingRedisHelper.clearShardingName();
        }
    }

    private <T> T execute(ResourceScriptSource scriptSource, List<String> key, Class<T> resultType, Object... param) {
        DefaultRedisScript<T> script = new DefaultRedisScript<>();
        script.setScriptSource(scriptSource);
        script.setResultType(resultType);
        return shardingRedisHelper.getRedisTemplate().execute(script, key, param);
    }

}
