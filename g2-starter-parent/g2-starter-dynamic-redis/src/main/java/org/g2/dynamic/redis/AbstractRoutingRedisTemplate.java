package org.g2.dynamic.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.script.ScriptExecutor;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;

import java.io.Closeable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2021-12-07
 */
public abstract class AbstractRoutingRedisTemplate<K, V> extends RedisTemplate<K, V> {

    private Map<Object, RedisTemplate<K, V>> redisTemplates;

    private RedisTemplate<K, V> defaultRedisTemplate;

    //
    //                  重写redisTemplate方法
    // ====================================================================

    @Override
    public <T> T execute(@NonNull RedisCallback<T> action) {
        return this.determineTargetRedisTemplate().execute(action);
    }

    @Override
    public <T> T execute(@NonNull RedisCallback<T> action, boolean exposeConnection) {
        return this.determineTargetRedisTemplate().execute(action, exposeConnection);
    }

    @Override
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
        return this.determineTargetRedisTemplate().execute(action, exposeConnection, pipeline);
    }

    @Override
    public <T> T execute(SessionCallback<T> session) {
        return this.determineTargetRedisTemplate().execute(session);
    }

    @Override
    @NonNull
    public List<Object> executePipelined(@NonNull SessionCallback<?> session) {
        return this.determineTargetRedisTemplate().executePipelined(session);
    }

    @Override
    @NonNull
    public List<Object> executePipelined(SessionCallback<?> session, RedisSerializer<?> resultSerializer) {
        return this.determineTargetRedisTemplate().executePipelined(session, resultSerializer);
    }

    @Override
    @NonNull
    public List<Object> executePipelined(@NonNull RedisCallback<?> action) {
        return this.determineTargetRedisTemplate().executePipelined(action);
    }

    @Override
    @NonNull
    public List<Object> executePipelined(RedisCallback<?> action, RedisSerializer<?> resultSerializer) {
        return this.determineTargetRedisTemplate().executePipelined(action, resultSerializer);
    }

    @Override
    public <T> T execute(@NonNull RedisScript<T> script, @NonNull List<K> keys, @NonNull Object... args) {
        return this.determineTargetRedisTemplate().execute(script, keys, args);
    }

    @Override
    public <T> T execute(@NonNull RedisScript<T> script, @NonNull RedisSerializer<?> argsSerializer, @NonNull RedisSerializer<T> resultSerializer, @NonNull List<K> keys, @NonNull Object... args) {
        return this.determineTargetRedisTemplate().execute(script, argsSerializer, resultSerializer, keys, args);
    }

    @Override
    public <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> callback) {
        return this.determineTargetRedisTemplate().executeWithStickyConnection(callback);
    }

    @Override
    public boolean isExposeConnection() {
        return this.determineTargetRedisTemplate().isExposeConnection();
    }

    @Override
    public void setExposeConnection(boolean exposeConnection) {
        this.determineTargetRedisTemplate().setExposeConnection(exposeConnection);
    }

    @Override
    public boolean isEnableDefaultSerializer() {
        return this.determineTargetRedisTemplate().isEnableDefaultSerializer();
    }

    @Override
    public void setEnableDefaultSerializer(boolean enableDefaultSerializer) {
        this.determineTargetRedisTemplate().setEnableDefaultSerializer(enableDefaultSerializer);
    }

    @Override
    public RedisSerializer<?> getDefaultSerializer() {
        return this.determineTargetRedisTemplate().getDefaultSerializer();
    }

    @Override
    public void setDefaultSerializer(@NonNull RedisSerializer<?> serializer) {
        this.determineTargetRedisTemplate().setDefaultSerializer(serializer);
    }

    @Override
    public void setKeySerializer(@NonNull RedisSerializer<?> serializer) {
        this.determineTargetRedisTemplate().setKeySerializer(serializer);
    }

    @Override
    @NonNull
    public RedisSerializer<?> getKeySerializer() {
        return this.determineTargetRedisTemplate().getKeySerializer();
    }

    @Override
    public void setValueSerializer(@NonNull RedisSerializer<?> serializer) {
        this.determineTargetRedisTemplate().setValueSerializer(serializer);
    }

    @Override
    @NonNull
    public RedisSerializer<?> getValueSerializer() {
        return this.determineTargetRedisTemplate().getValueSerializer();
    }

    @Override
    @NonNull
    public RedisSerializer<?> getHashKeySerializer() {
        return this.determineTargetRedisTemplate().getHashKeySerializer();
    }

    @Override
    public void setHashKeySerializer(@NonNull RedisSerializer<?> hashKeySerializer) {
        this.determineTargetRedisTemplate().setHashKeySerializer(hashKeySerializer);
    }

    @Override
    @NonNull
    public RedisSerializer<?> getHashValueSerializer() {
        return this.determineTargetRedisTemplate().getHashValueSerializer();
    }

    @Override
    public void setHashValueSerializer(@NonNull RedisSerializer<?> hashValueSerializer) {
        this.determineTargetRedisTemplate().setHashValueSerializer(hashValueSerializer);
    }

    @Override
    @NonNull
    public RedisSerializer<String> getStringSerializer() {
        return this.determineTargetRedisTemplate().getStringSerializer();
    }

    @Override
    public void setStringSerializer(@NonNull RedisSerializer<String> stringSerializer) {
        this.determineTargetRedisTemplate().setStringSerializer(stringSerializer);
    }

    @Override
    public void setScriptExecutor(@NonNull ScriptExecutor<K> scriptExecutor) {
        this.determineTargetRedisTemplate().setScriptExecutor(scriptExecutor);
    }

    @Override
    @NonNull
    public List<Object> exec() {
        return this.determineTargetRedisTemplate().exec();
    }

    @Override
    @NonNull
    public List<Object> exec(@NonNull RedisSerializer<?> valueSerializer) {
        return this.determineTargetRedisTemplate().exec(valueSerializer);
    }

    @Override
    public Boolean delete(K key) {
        return this.determineTargetRedisTemplate().delete(key);
    }

    @Override
    public Long delete(@NonNull Collection<K> keys) {
        return this.determineTargetRedisTemplate().delete(keys);
    }

    @Override
    public Boolean hasKey(K key) {
        return this.determineTargetRedisTemplate().hasKey(key);
    }

    @Override
    public Boolean expire(K key, long timeout, TimeUnit unit) {
        return this.determineTargetRedisTemplate().expire(key, timeout, unit);
    }

    @Override
    public Boolean expireAt(K key, Date date) {
        return this.determineTargetRedisTemplate().expireAt(key, date);
    }

    @Override
    public void convertAndSend(@NonNull String channel, @NonNull Object message) {
        this.determineTargetRedisTemplate().convertAndSend(channel, message);
    }

    @Override
    public Long getExpire(K key) {
        return this.determineTargetRedisTemplate().getExpire(key);
    }

    @Override
    public Long getExpire(K key, @NonNull TimeUnit timeUnit) {
        return this.determineTargetRedisTemplate().getExpire(key, timeUnit);
    }

    @Override
    public Set<K> keys(K pattern) {
        return this.determineTargetRedisTemplate().keys(pattern);
    }

    @Override
    public Boolean persist(K key) {
        return this.determineTargetRedisTemplate().persist(key);
    }

    @Override
    public Boolean move(K key, int dbIndex) {
        return this.determineTargetRedisTemplate().move(key, dbIndex);
    }

    @Override
    public K randomKey() {
        return this.determineTargetRedisTemplate().randomKey();
    }

    @Override
    public void rename(K oldKey, K newKey) {
        this.determineTargetRedisTemplate().rename(oldKey, newKey);
    }

    @Override
    public Boolean renameIfAbsent(K oldKey, K newKey) {
        return this.determineTargetRedisTemplate().renameIfAbsent(oldKey, newKey);
    }

    @Override
    public DataType type(K key) {
        return this.determineTargetRedisTemplate().type(key);
    }

    @Override
    public byte[] dump(K key) {
        return this.determineTargetRedisTemplate().dump(key);
    }

    @Override
    public void restore(@NonNull K key, @NonNull byte[] value, long timeToLive, @NonNull TimeUnit unit) {
        this.determineTargetRedisTemplate().restore(key, value, timeToLive, unit);
    }

    @Override
    public void multi() {
        this.determineTargetRedisTemplate().multi();
    }

    @Override
    public void discard() {
        this.determineTargetRedisTemplate().discard();
    }

    @Override
    public void watch(K key) {
        this.determineTargetRedisTemplate().watch(key);
    }

    @Override
    public void watch(Collection<K> keys) {
        this.determineTargetRedisTemplate().watch(keys);
    }

    @Override
    public void unwatch() {
        this.determineTargetRedisTemplate().unwatch();
    }

    @Override
    public List<V> sort(@NonNull SortQuery<K> query) {
        return this.determineTargetRedisTemplate().sort(query);
    }

    @Override
    public <T> List<T> sort(SortQuery<K> query, RedisSerializer<T> resultSerializer) {
        return this.determineTargetRedisTemplate().sort(query, resultSerializer);
    }

    @Override
    public <T> List<T> sort(@NonNull SortQuery<K> query, @NonNull BulkMapper<T, V> bulkMapper) {
        return this.determineTargetRedisTemplate().sort(query, bulkMapper);
    }

    @Override
    public <T, S> List<T> sort(@NonNull SortQuery<K> query, @NonNull BulkMapper<T, S> bulkMapper, RedisSerializer<S> resultSerializer) {
        return this.determineTargetRedisTemplate().sort(query, bulkMapper, resultSerializer);
    }

    @Override
    public Long sort(SortQuery<K> query, K storeKey) {
        return this.determineTargetRedisTemplate().sort(query, storeKey);
    }

    @Override
    @NonNull
    public BoundValueOperations<K, V> boundValueOps(@NonNull K key) {
        return this.determineTargetRedisTemplate().boundValueOps(key);
    }

    @Override
    @NonNull
    public ValueOperations<K, V> opsForValue() {
        return this.determineTargetRedisTemplate().opsForValue();
    }

    @Override
    @NonNull
    public ListOperations<K, V> opsForList() {
        return this.determineTargetRedisTemplate().opsForList();
    }

    @Override
    @NonNull
    public BoundListOperations<K, V> boundListOps(@NonNull K key) {
        return this.determineTargetRedisTemplate().boundListOps(key);
    }

    @Override
    @NonNull
    public BoundSetOperations<K, V> boundSetOps(@NonNull K key) {
        return this.determineTargetRedisTemplate().boundSetOps(key);
    }

    @Override
    @NonNull
    public SetOperations<K, V> opsForSet() {
        return this.determineTargetRedisTemplate().opsForSet();
    }

    @Override
    @NonNull
    public BoundZSetOperations<K, V> boundZSetOps(@NonNull K key) {
        return this.determineTargetRedisTemplate().boundZSetOps(key);
    }

    @Override
    @NonNull
    public ZSetOperations<K, V> opsForZSet() {
        return this.determineTargetRedisTemplate().opsForZSet();
    }

    @Override
    @NonNull
    public GeoOperations<K, V> opsForGeo() {
        return this.determineTargetRedisTemplate().opsForGeo();
    }

    @Override
    @NonNull
    public BoundGeoOperations<K, V> boundGeoOps(@NonNull K key) {
        return this.determineTargetRedisTemplate().boundGeoOps(key);
    }

    @Override
    @NonNull
    public HyperLogLogOperations<K, V> opsForHyperLogLog() {
        return this.determineTargetRedisTemplate().opsForHyperLogLog();
    }

    @Override
    @NonNull
    public <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(@NonNull K key) {
        return this.determineTargetRedisTemplate().boundHashOps(key);
    }

    @Override
    @NonNull
    public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
        return this.determineTargetRedisTemplate().opsForHash();
    }

    @Override
    @NonNull
    public ClusterOperations<K, V> opsForCluster() {
        return this.determineTargetRedisTemplate().opsForCluster();
    }

    @Override
    public void killClient(@NonNull String host, int port) {
        this.determineTargetRedisTemplate().killClient(host, port);
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return this.determineTargetRedisTemplate().getClientList();
    }

    @Override
    public void slaveOf(@NonNull String host, int port) {
        this.determineTargetRedisTemplate().slaveOf(host, port);
    }

    @Override
    public void slaveOfNoOne() {
        this.determineTargetRedisTemplate().slaveOfNoOne();
    }

    @Override
    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        this.determineTargetRedisTemplate().setEnableTransactionSupport(enableTransactionSupport);
    }

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.determineTargetRedisTemplate().setBeanClassLoader(classLoader);
    }

    @Override
    public RedisConnectionFactory getConnectionFactory() {
        return this.determineTargetRedisTemplate().getConnectionFactory();
    }

    @Override
    public void setConnectionFactory(@NonNull RedisConnectionFactory connectionFactory) {
        this.determineTargetRedisTemplate().setConnectionFactory(connectionFactory);
    }

    //
    //                                  自定义方法
    // =====================================================================================

    public void setRedisTemplates(Map<Object, RedisTemplate<K, V>> redisTemplates) {
        this.redisTemplates = redisTemplates;
    }

    public void setDefaultRedisTemplate(RedisTemplate<K, V> defaultRedisTemplate) {
        this.defaultRedisTemplate = defaultRedisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (this.redisTemplates == null) {
            throw new IllegalArgumentException("Property 'redisTemplates' is required");
        }
        if (this.defaultRedisTemplate == null) {
            throw new IllegalArgumentException("Property 'defaultRedisTemplate' is required");
        }
    }

    /**
     * 获取动态redisTemplate
     */
    private RedisTemplate<K, V> determineTargetRedisTemplate() {
        Object lookupKey = determineCurrentLookupKey();
        if (lookupKey == null) {
            return this.defaultRedisTemplate;
        }
        RedisTemplate<K, V> redisTemplate = redisTemplates.get(lookupKey);
        if (redisTemplate == null) {
            redisTemplate = createRedisTemplateOnMissing(lookupKey);
            redisTemplates.put(lookupKey, redisTemplate);
        }
        return redisTemplate;
    }

    /**
     * 查询当前动态对象
     *
     * @return 动态对象
     */
    protected abstract Object determineCurrentLookupKey();

    /**
     * 根据对象创建动态redisTemplate
     *
     * @param lookupKey 动态对象
     * @return redisTemplate
     */
    protected abstract RedisTemplate<K, V> createRedisTemplateOnMissing(Object lookupKey);
}
