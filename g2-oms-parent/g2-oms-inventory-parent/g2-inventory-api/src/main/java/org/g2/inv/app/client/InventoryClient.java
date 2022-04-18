package org.g2.inv.app.client;

import org.g2.core.helper.AsyncTaskHelper;
import org.g2.dynamic.redis.hepler.sharding.ShardingRedisHelper;
import org.g2.dynamic.redis.hepler.sharding.ShardingRedisTemplate;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.repository.StockRedisRepository;
import org.g2.inv.core.infra.constant.InvCoreConstant;
import org.g2.inv.core.infra.repository.impl.BaseStockRedisRepository;
import org.g2.inv.domain.repository.InvOperationStockRepository;
import org.g2.inv.domain.repository.InvClientTransactionRepository;
import org.g2.inv.infra.feign.InvConsoleFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author wuwenxi 2022-04-18
 */
@Component
public class InventoryClient extends BaseStockRedisRepository
        implements InvOperationStockRepository, InvClientTransactionRepository, StockRedisRepository {

    private static final Logger logger = LoggerFactory.getLogger(InventoryClient.class);

    private final ShardingRedisHelper shardingRedisHelper;
    private final InvConsoleFeignClient invConsoleFeignClient;

    public InventoryClient(ShardingRedisHelper shardingRedisHelper, InvConsoleFeignClient invConsoleFeignClient) {
        super(shardingRedisHelper, logger);
        this.shardingRedisHelper = shardingRedisHelper;
        this.invConsoleFeignClient = invConsoleFeignClient;
    }

    @Override
    public Map<String, Long> readPosStock(String posCode, Collection<String> skuCodes) {
        Map<String, Long> result = new HashMap<>(skuCodes.size() * 2);
        return operation(posCode, () -> {
            ShardingRedisTemplate<String, String> redisTemplate = shardingRedisHelper.getRedisTemplate();
            byte[] atsKey = shardingRedisHelper.serialize(InvCoreConstant.InvStockKey.HASH_EXTEND_STOCK_ATS);
            // 执行pipelined
            redisTemplate.executePipelined((RedisCallback<?>) connection -> {
                for (String skuCode : skuCodes) {
                    byte[] posStockKey = shardingRedisHelper.serialize(String.format(InvCoreConstant.RedisKeyFormat.STOCK_LEVEL_KEY, posCode, skuCode));
                    byte[] bytes = connection.hGet(posStockKey, atsKey);
                    long ats = 0;
                    if (bytes != null) {
                        ats = Long.parseLong(Arrays.toString(bytes));
                    }
                    result.put(skuCode, ats);
                }
                return null;
            });
            return result;
        });
    }

    @Override
    public <T> Map<String, Long> readPosStock(String posCode, Collection<T> collectionT, IFunction<T, String, String> skuCodeFunction) {
        return readPosStock(posCode, skuCodeFunction.apply(collectionT, posCode));
    }

    @Override
    public <K, T> Map<String, Map<String, Long>> readPosStock(Collection<K> collectionK, Collection<T> collectionT, Function<K, String> posCodeFunction, IFunction<T, String, String> skuCodeFunction) {
        Map<String, Map<String, Long>> result = new HashMap<>(16);
        for (K k : collectionK) {
            String posCode = posCodeFunction.apply(k);
            result.computeIfAbsent(posCode, s -> new HashMap<>(16))
                    .putAll(readPosStock(posCode, collectionT, skuCodeFunction));
        }
        return result;
    }

    @Override
    public <T> boolean checkStockEnough(String posCode, Collection<T> collectionT, Function<String, Long> skuCodeQuantityFunction, IFunction<T, String, String> skuCodeFunction) {
        Collection<String> skuCodes = skuCodeFunction.apply(collectionT, posCode);
        Map<String, Long> readPosStock = readPosStock(posCode, skuCodes);
        return skuCodes.stream().anyMatch(e -> readPosStock.get(e) < skuCodeQuantityFunction.apply(e));
    }

    @Override
    public <K, T> Collection<K> checkStockEnough(Collection<K> collectionK, Collection<T> collectionT, Function<K, String> posCodeFunction, Function<String, Long> skuCodeQuantityFunction, IFunction<T, String, String> skuCodeFunction) {
        List<K> result = new ArrayList<>();
        for (K k : collectionK) {
            String posCode = posCodeFunction.apply(k);
            if (checkStockEnough(posCode, collectionT, skuCodeQuantityFunction, skuCodeFunction)) {
                result.add(k);
            }
        }
        return result;
    }

    //
    //   implements InvClientTransactionRepository
    // ===================================================

    @Override
    public void saveInvTransaction(InvTransaction invTransaction) {
        this.saveInvTransactions(Collections.singletonList(invTransaction));
    }

    @Override
    public void saveInvTransactions(List<InvTransaction> invTransactions) {
        invTransactions.forEach(InvTransaction::check);
        AsyncTaskHelper.staticOperation(() -> invConsoleFeignClient.save(invTransactions));
    }
}
