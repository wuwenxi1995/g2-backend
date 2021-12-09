package org.g2.dynamic.redis.hepler.sharding;

import org.g2.dynamic.redis.AbstractRoutingRedisTemplate;
import org.g2.dynamic.redis.CustomizerRedisTemplateFactory;
import org.g2.dynamic.redis.util.DatabaseThreadLocal;
import org.g2.dynamic.redis.util.RedisShardingTheadLocal;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author wuwenxi 2021-12-07
 */
public class ShardingRedisTemplate<K, V> extends AbstractRoutingRedisTemplate<K, V> {

    private CustomizerRedisTemplateFactory<K, V> customizerRedisTemplateFactory;

    public ShardingRedisTemplate(CustomizerRedisTemplateFactory<K, V> customizerRedisTemplateFactory) {
        this.customizerRedisTemplateFactory = customizerRedisTemplateFactory;
    }

    @Override
    protected boolean isCluster() {
        return customizerRedisTemplateFactory.isCluster();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return RedisShardingTheadLocal.get();
    }

    @Override
    public RedisTemplate<K, V> createRedisTemplateOnMissing(Object lookupKey) {
        Integer database;
        return customizerRedisTemplateFactory.createRedisTemplate((database = DatabaseThreadLocal.get()) == null ? 0 : database);
    }
}
