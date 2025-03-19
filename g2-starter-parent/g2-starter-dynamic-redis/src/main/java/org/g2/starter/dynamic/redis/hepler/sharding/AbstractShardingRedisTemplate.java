package org.g2.starter.dynamic.redis.hepler.sharding;

import org.g2.starter.dynamic.redis.AbstractRoutingRedisTemplate;
import org.g2.starter.dynamic.redis.CustomizerRedisTemplateFactory;
import org.g2.starter.dynamic.redis.config.properties.DynamicRedisProperties;
import org.g2.starter.dynamic.redis.util.DatabaseThreadLocal;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuwenxi 2021-12-24
 */
public class AbstractShardingRedisTemplate<K, V> extends AbstractRoutingRedisTemplate<K, V> {

    private static final AtomicInteger COUNT = new AtomicInteger(0);
    private final Map<Integer, List<RedisTemplate<K, V>>> redisTemplates = new HashMap<>(16);

    private final CustomizerRedisTemplateFactory<K, V> customizerRedisTemplateFactory;
    private final DynamicRedisProperties dynamicRedisProperties;

    public AbstractShardingRedisTemplate(CustomizerRedisTemplateFactory<K, V> customizerRedisTemplateFactory, DynamicRedisProperties dynamicRedisProperties) {
        this.customizerRedisTemplateFactory = customizerRedisTemplateFactory;
        this.dynamicRedisProperties = dynamicRedisProperties;
    }

    @Override
    protected RedisTemplate<K, V> determineTargetRedisTemplate() {
        Integer db;
        db = (db = DatabaseThreadLocal.get()) == null ? 0 : db;
        List<RedisTemplate<K, V>> redisTemplates = this.redisTemplates.get(db);
        RedisTemplate<K, V> redisTemplate = null;
        boolean createNew = false;
        if (redisTemplates == null || redisTemplates.size() == 0) {
            synchronized (this.redisTemplates) {
                redisTemplates = this.redisTemplates.get(db);
                if (redisTemplates == null) {
                    // 创建新的数据源
                    redisTemplate = createRedisTemplateOnMissing(db);
                    // 加入缓存
                    redisTemplates = new ArrayList<>();
                    redisTemplates.add(redisTemplate);
                    this.redisTemplates.put(db, redisTemplates);
                    createNew = true;
                }
            }
        }
        if (!createNew && redisTemplates.size() < dynamicRedisProperties.getShardingNum()) {
            // 创建新的数据源
            redisTemplate = createRedisTemplateOnMissing(db);
            // 加入缓存
            redisTemplates.add(redisTemplate);
            this.redisTemplates.put(db, redisTemplates);
        }
        if (redisTemplate == null) {
            // 实现自己的算法
            redisTemplate = routing(redisTemplates);
        }
        return redisTemplate;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    @Override
    protected RedisTemplate<K, V> createRedisTemplateOnMissing(Object lookupKey) {
        return customizerRedisTemplateFactory.createRedisTemplate((int) lookupKey);
    }

    /**
     * 默认实现轮询算法
     */
    protected RedisTemplate<K, V> routing(List<RedisTemplate<K, V>> redisTemplates) {
        int current, next;
        do {
            current = COUNT.get();
            if (current == dynamicRedisProperties.getShardingNum() - 1) {
                next = 0;
            } else {
                next = current + 1;
            }
        } while (!COUNT.compareAndSet(current, next));
        return redisTemplates.get(current);
    }
}
