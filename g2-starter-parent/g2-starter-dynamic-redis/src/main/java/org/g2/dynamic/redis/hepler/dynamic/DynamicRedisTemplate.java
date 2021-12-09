package org.g2.dynamic.redis.hepler.dynamic;

import org.g2.dynamic.redis.AbstractRoutingRedisTemplate;
import org.g2.dynamic.redis.CustomizerRedisTemplateFactory;
import org.g2.dynamic.redis.util.DatabaseThreadLocal;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author wuwenxi 2021-12-07
 */
public class DynamicRedisTemplate<K, V> extends AbstractRoutingRedisTemplate<K, V> {

    private CustomizerRedisTemplateFactory<K, V> customizerRedisTemplateFactory;

    public DynamicRedisTemplate(CustomizerRedisTemplateFactory<K, V> customizerRedisTemplateFactory) {
        this.customizerRedisTemplateFactory = customizerRedisTemplateFactory;
    }

    @Override
    protected boolean isCluster() {
        return customizerRedisTemplateFactory.isCluster();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DatabaseThreadLocal.get();
    }

    @Override
    public RedisTemplate<K, V> createRedisTemplateOnMissing(Object lookupKey) {
        return customizerRedisTemplateFactory.createRedisTemplate((int) lookupKey);
    }
}
