package org.g2.dynamic.redis.hepler.sharding;

import org.g2.dynamic.redis.AbstractRoutingRedisTemplate;
import org.g2.dynamic.redis.CustomizerRedisTemplateFactory;
import org.g2.dynamic.redis.hepler.sharding.cache.Cache;
import org.g2.dynamic.redis.util.DatabaseThreadLocal;
import org.g2.dynamic.redis.util.RedisShardingTheadLocal;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author wuwenxi 2021-12-07
 */
public class ShardingRedisTemplate<K, V> extends AbstractRoutingRedisTemplate<K, V> {

    private final Cache<RedisTemplate<K, V>> redisTemplates = new Cache<>();

    private final CustomizerRedisTemplateFactory<K, V> customizerRedisTemplateFactory;

    public ShardingRedisTemplate(CustomizerRedisTemplateFactory<K, V> customizerRedisTemplateFactory) {
        this.customizerRedisTemplateFactory = customizerRedisTemplateFactory;
    }

    @Override
    protected RedisTemplate<K, V> determineTargetRedisTemplate() {
        Object lookupKey = determineCurrentLookupKey();
        if (lookupKey == null) {
            return getDefaultRedisTemplate();
        }
        RedisTemplate<K, V> redisTemplate = redisTemplates.get(lookupKey);
        if (redisTemplate == null) {
            RedisTemplate<K, V> createNewTemplate = createRedisTemplateOnMissing(lookupKey);
            redisTemplate = redisTemplates.put(lookupKey, createNewTemplate);
            if (!createNewTemplate.equals(redisTemplate)) {
                createNewTemplate.getRequiredConnectionFactory().getConnection().close();
            }
        }
        return redisTemplate;
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
