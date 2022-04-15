package org.g2.inv.core.infra.repository.impl;

import org.g2.dynamic.redis.hepler.sharding.ShardingRedisHelper;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRedisRepository;
import org.g2.inv.core.infra.constant.InvCoreConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;

/**
 * @author wuwenxi 2022-04-13
 */
@Repository
public class StockLevelRedisRepositoryImpl extends BaseStockRedisRepository implements StockLevelRedisRepository {

    private static final Logger log = LoggerFactory.getLogger(StockLevelRedisRepositoryImpl.class);
    private static final DefaultRedisScript<Void> SYNC_STOCK_SCRIPT;

    private static final ResourceScriptSource SYNC_STOCK_LEVEL = new ResourceScriptSource(new ClassPathResource("/script/lua/sync-stock-level.lua"));

    public StockLevelRedisRepositoryImpl(ShardingRedisHelper shardingRedisHelper) {
        super(shardingRedisHelper, log);
    }

    @Override
    public void sync(StockLevel stockLevel) {
        operation(stockLevel.getPosCode(), () -> execute(SYNC_STOCK_SCRIPT, null,
                String.valueOf(stockLevel.getOnHand() - stockLevel.getReserved()),
                String.format(InvCoreConstant.RedisKeyFormat.STOCK_LEVEL_KEY, stockLevel.getPosCode(), stockLevel.getSkuCode()),
                InvCoreConstant.InvStockKey.HASH_STOCK_LEVEL,
                InvCoreConstant.InvStockKey.HASH_EXTEND_STOCK_RESERVED,
                InvCoreConstant.InvStockKey.HASH_EXTEND_STOCK_ATS
        ));
    }

    static {
        SYNC_STOCK_SCRIPT = new DefaultRedisScript<>();
        SYNC_STOCK_SCRIPT.setScriptSource(SYNC_STOCK_LEVEL);
    }
}
