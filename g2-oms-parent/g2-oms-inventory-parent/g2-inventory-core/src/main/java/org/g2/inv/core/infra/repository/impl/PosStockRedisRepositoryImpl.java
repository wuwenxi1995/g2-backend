package org.g2.inv.core.infra.repository.impl;

import com.alibaba.fastjson.JSONObject;
import org.g2.dynamic.redis.hepler.sharding.ShardingRedisHelper;
import org.g2.inv.core.domain.repository.PosStockRedisRepository;
import org.g2.inv.core.domain.vo.StockQueryResponseVO;
import org.g2.inv.core.infra.constant.InvCoreConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author wuwenxi 2022-04-15
 */
@Repository
public class PosStockRedisRepositoryImpl extends BaseStockRedisRepository implements PosStockRedisRepository {

    private static final Logger log = LoggerFactory.getLogger(PosStockRedisRepositoryImpl.class);

    private static final DefaultRedisScript<Boolean> POS_STOCK_OCCUPY_SCRIPT;
    private static final DefaultRedisScript<Boolean> POS_STOCK_RELEASE_SCRIPT;
    private static final DefaultRedisScript<Boolean> POS_STOCK_REDUCE_SCRIPT;

    private final ShardingRedisHelper shardingRedisHelper;

    public PosStockRedisRepositoryImpl(ShardingRedisHelper shardingRedisHelper) {
        super(shardingRedisHelper, log);
        this.shardingRedisHelper = shardingRedisHelper;
    }

    @Override
    public List<StockQueryResponseVO> readPosStock(String posCode, Map<String, Long> skuList) {
        return operation(posCode, () -> {
            List<StockQueryResponseVO> responseList = new ArrayList<>();
            RedisSerializer<String> serializer = shardingRedisHelper.getRedisTemplate().getStringSerializer();
            byte[] hashKey = serializer.serialize(InvCoreConstant.InvStockKey.HASH_EXTEND_STOCK_ATS);
            // pipeline 执行
            Map<String, Long> result = new HashMap<>(skuList.size() * 2);
            shardingRedisHelper.getRedisTemplate().executePipelined((RedisCallback<?>) connection -> {
                skuList.forEach((skuCode, quantity) -> {
                    String stockLevelKey = String.format(InvCoreConstant.RedisKeyFormat.STOCK_LEVEL_KEY, posCode, skuCode);
                    byte[] key = serializer.serialize(stockLevelKey);
                    byte[] bytes = connection.hGet(key, hashKey);
                    long ats = 0L;
                    if (bytes != null) {
                        ats = Long.parseLong(Arrays.toString(bytes));
                    }
                    result.put(skuCode, ats);
                });
                return null;
            });
            skuList.forEach((skuCode, quantity) -> {
                StockQueryResponseVO responseVO = new StockQueryResponseVO();
                responseVO.setSkuCode(skuCode);
                Long ats;
                ats = (ats = result.get(skuCode)) == null ? 0 : ats;
                responseVO.setAvailable(ats > quantity);
                responseList.add(responseVO);
            });
            return responseList;
        });
    }

    @Override
    public boolean occupy(String posCode, Map<String, Long> items) {
        String data = JSONObject.toJSONString(items);
        log.info("occupy posStock before : {}", data);
        return execute(posCode, data, POS_STOCK_OCCUPY_SCRIPT);
    }

    @Override
    public boolean release(String posCode, Map<String, Long> items) {
        String data = JSONObject.toJSONString(items);
        log.info("release posStock before : {}", data);
        return execute(posCode, data, POS_STOCK_RELEASE_SCRIPT);
    }

    @Override
    public boolean reduce(String posCode, Map<String, Long> items) {
        String data = JSONObject.toJSONString(items);
        log.info("reduce posStock before : {}", data);
        return execute(posCode, data, POS_STOCK_REDUCE_SCRIPT);
    }

    private boolean execute(String posCode, String data, RedisScript<Boolean> redisScript) {
        return operation(posCode, () -> execute(redisScript,
                Collections.singletonList(String.format(InvCoreConstant.RedisKeyFormat.STOCK_LEVEL_FORMAT, posCode)),
                InvCoreConstant.InvStockKey.HASH_STOCK_LEVEL,
                InvCoreConstant.InvStockKey.HASH_EXTEND_STOCK_RESERVED,
                InvCoreConstant.InvStockKey.HASH_EXTEND_STOCK_ATS,
                data, posCode, InvCoreConstant.MessageQueue.TRIGGER_POS_STOCK_KEY));
    }

    static {
        POS_STOCK_OCCUPY_SCRIPT = new DefaultRedisScript<>();
        POS_STOCK_OCCUPY_SCRIPT.setScriptSource(InvCoreConstant.RedisScript.POS_STOCK_OCCUPY_SCRIPT);

        POS_STOCK_RELEASE_SCRIPT = new DefaultRedisScript<>();
        POS_STOCK_RELEASE_SCRIPT.setScriptSource(InvCoreConstant.RedisScript.POS_STOCK_RELEASE_SCRIPT);

        POS_STOCK_REDUCE_SCRIPT = new DefaultRedisScript<>();
        POS_STOCK_REDUCE_SCRIPT.setScriptSource(InvCoreConstant.RedisScript.POS_STOCK_REDUCE_SCRIPT);
    }
}
