package org.g2.starter.redis.client;

import java.io.Closeable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.g2.starter.redis.infra.hepler.RedisShardingThreadLocal;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundStreamOperations;
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
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.script.ScriptExecutor;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
public class RedisShardingClient extends RedisTemplate<String, String> {

    private Map<Object, StringRedisTemplate> redisTemplates;

    private StringRedisTemplate defaultRedisTemplate;

    public RedisShardingClient() {
    }

    //
    //                  重写redisTemplate方法
    // ====================================================================

    @Override
    public <T> T execute(@NonNull RedisCallback<T> action) {
        return determineTargetRedisTemplate().execute(action);
    }

    @Override
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection) {
        return determineTargetRedisTemplate().execute(action, exposeConnection);
    }

    @Override
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
        return determineTargetRedisTemplate().execute(action, exposeConnection, pipeline);
    }

    @Override
    public <T> T execute(SessionCallback<T> session) {
        return super.execute(session);
    }

    @Override
    @NonNull
    public List<Object> executePipelined(SessionCallback<?> session) {
        return super.executePipelined(session);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session, RedisSerializer<?> resultSerializer) {
        return super.executePipelined(session, resultSerializer);
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action) {
        return super.executePipelined(action);
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action, RedisSerializer<?> resultSerializer) {
        return super.executePipelined(action, resultSerializer);
    }

    @Override
    public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        return super.execute(script, keys, args);
    }

    @Override
    public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer, List<String> keys, Object... args) {
        return super.execute(script, argsSerializer, resultSerializer, keys, args);
    }

    @Override
    public <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> callback) {
        return super.executeWithStickyConnection(callback);
    }

    @Override
    protected RedisConnection createRedisConnectionProxy(RedisConnection pm) {
        return super.createRedisConnectionProxy(pm);
    }

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return super.preProcessConnection(connection, existingConnection);
    }

    @Override
    protected <T> T postProcessResult(T result, RedisConnection conn, boolean existingConnection) {
        return super.postProcessResult(result, conn, existingConnection);
    }

    @Override
    public boolean isExposeConnection() {
        return super.isExposeConnection();
    }

    @Override
    public void setExposeConnection(boolean exposeConnection) {
        super.setExposeConnection(exposeConnection);
    }

    @Override
    public boolean isEnableDefaultSerializer() {
        return super.isEnableDefaultSerializer();
    }

    @Override
    public void setEnableDefaultSerializer(boolean enableDefaultSerializer) {
        super.setEnableDefaultSerializer(enableDefaultSerializer);
    }

    @Override
    public RedisSerializer<?> getDefaultSerializer() {
        return super.getDefaultSerializer();
    }

    @Override
    public void setDefaultSerializer(RedisSerializer<?> serializer) {
        super.setDefaultSerializer(serializer);
    }

    @Override
    public void setKeySerializer(RedisSerializer<?> serializer) {
        super.setKeySerializer(serializer);
    }

    @Override
    public RedisSerializer<?> getKeySerializer() {
        return super.getKeySerializer();
    }

    @Override
    public void setValueSerializer(RedisSerializer<?> serializer) {
        super.setValueSerializer(serializer);
    }

    @Override
    public RedisSerializer<?> getValueSerializer() {
        return super.getValueSerializer();
    }

    @Override
    public RedisSerializer<?> getHashKeySerializer() {
        return super.getHashKeySerializer();
    }

    @Override
    public void setHashKeySerializer(RedisSerializer<?> hashKeySerializer) {
        super.setHashKeySerializer(hashKeySerializer);
    }

    @Override
    public RedisSerializer<?> getHashValueSerializer() {
        return super.getHashValueSerializer();
    }

    @Override
    public void setHashValueSerializer(RedisSerializer<?> hashValueSerializer) {
        super.setHashValueSerializer(hashValueSerializer);
    }

    @Override
    public RedisSerializer<String> getStringSerializer() {
        return super.getStringSerializer();
    }

    @Override
    public void setStringSerializer(RedisSerializer<String> stringSerializer) {
        super.setStringSerializer(stringSerializer);
    }

    @Override
    public void setScriptExecutor(ScriptExecutor<String> scriptExecutor) {
        super.setScriptExecutor(scriptExecutor);
    }

    @Override
    public List<Object> exec() {
        return super.exec();
    }

    @Override
    public List<Object> exec(RedisSerializer<?> valueSerializer) {
        return super.exec(valueSerializer);
    }

    @Override
    protected List<Object> execRaw() {
        return super.execRaw();
    }

    @Override
    public Boolean delete(String key) {
        return super.delete(key);
    }

    @Override
    public Long delete(Collection<String> keys) {
        return super.delete(keys);
    }

    @Override
    public Boolean unlink(String key) {
        return super.unlink(key);
    }

    @Override
    public Long unlink(Collection<String> keys) {
        return super.unlink(keys);
    }

    @Override
    public Boolean hasKey(@NonNull String key) {
        return determineTargetRedisTemplate().hasKey(key);
    }

    @Override
    public Long countExistingKeys(Collection<String> keys) {
        return super.countExistingKeys(keys);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return super.expire(key, timeout, unit);
    }

    @Override
    public Boolean expireAt(String key, Date date) {
        return super.expireAt(key, date);
    }

    @Override
    public void convertAndSend(String channel, Object message) {
        super.convertAndSend(channel, message);
    }

    @Override
    public Long getExpire(String key) {
        return super.getExpire(key);
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        return super.getExpire(key, timeUnit);
    }

    @Override
    public Set<String> keys(String pattern) {
        return super.keys(pattern);
    }

    @Override
    public Boolean persist(String key) {
        return super.persist(key);
    }

    @Override
    public Boolean move(String key, int dbIndex) {
        return super.move(key, dbIndex);
    }

    @Override
    public String randomKey() {
        return super.randomKey();
    }

    @Override
    public void rename(String oldKey, String newKey) {
        super.rename(oldKey, newKey);
    }

    @Override
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return super.renameIfAbsent(oldKey, newKey);
    }

    @Override
    public DataType type(String key) {
        return super.type(key);
    }

    @Override
    public byte[] dump(String key) {
        return super.dump(key);
    }

    @Override
    public void restore(String key, byte[] value, long timeToLive, TimeUnit unit, boolean replace) {
        super.restore(key, value, timeToLive, unit, replace);
    }

    @Override
    public void multi() {
        super.multi();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public void watch(String key) {
        super.watch(key);
    }

    @Override
    public void watch(Collection<String> keys) {
        super.watch(keys);
    }

    @Override
    public void unwatch() {
        super.unwatch();
    }

    @Override
    public List<String> sort(SortQuery<String> query) {
        return super.sort(query);
    }

    @Override
    public <T> List<T> sort(SortQuery<String> query, RedisSerializer<T> resultSerializer) {
        return super.sort(query, resultSerializer);
    }

    @Override
    public <T> List<T> sort(SortQuery<String> query, BulkMapper<T, String> bulkMapper) {
        return super.sort(query, bulkMapper);
    }

    @Override
    public <T, S> List<T> sort(SortQuery<String> query, BulkMapper<T, S> bulkMapper, RedisSerializer<S> resultSerializer) {
        return super.sort(query, bulkMapper, resultSerializer);
    }

    @Override
    public Long sort(SortQuery<String> query, String storeKey) {
        return super.sort(query, storeKey);
    }

    @Override
    public void killClient(String host, int port) {
        super.killClient(host, port);
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return super.getClientList();
    }

    @Override
    public void slaveOf(String host, int port) {
        super.slaveOf(host, port);
    }

    @Override
    public void slaveOfNoOne() {
        super.slaveOfNoOne();
    }

    @Override
    public ClusterOperations<String, String> opsForCluster() {
        return super.opsForCluster();
    }

    @Override
    public GeoOperations<String, String> opsForGeo() {
        return super.opsForGeo();
    }

    @Override
    public BoundGeoOperations<String, String> boundGeoOps(String key) {
        return super.boundGeoOps(key);
    }

    @Override
    public <HK, HV> BoundHashOperations<String, HK, HV> boundHashOps(String key) {
        return super.boundHashOps(key);
    }

    @Override
    public <HK, HV> HashOperations<String, HK, HV> opsForHash() {
        return super.opsForHash();
    }

    @Override
    public HyperLogLogOperations<String, String> opsForHyperLogLog() {
        return super.opsForHyperLogLog();
    }

    @Override
    public ListOperations<String, String> opsForList() {
        return super.opsForList();
    }

    @Override
    public BoundListOperations<String, String> boundListOps(String key) {
        return super.boundListOps(key);
    }

    @Override
    public BoundSetOperations<String, String> boundSetOps(String key) {
        return super.boundSetOps(key);
    }

    @Override
    public SetOperations<String, String> opsForSet() {
        return super.opsForSet();
    }

    @Override
    public <HK, HV> StreamOperations<String, HK, HV> opsForStream() {
        return super.opsForStream();
    }

    @Override
    public <HK, HV> StreamOperations<String, HK, HV> opsForStream(HashMapper<? super String, ? super HK, ? super HV> hashMapper) {
        return super.opsForStream(hashMapper);
    }

    @Override
    public <HK, HV> BoundStreamOperations<String, HK, HV> boundStreamOps(String key) {
        return super.boundStreamOps(key);
    }

    @Override
    public BoundValueOperations<String, String> boundValueOps(String key) {
        return super.boundValueOps(key);
    }

    @Override
    public ValueOperations<String, String> opsForValue() {
        return super.opsForValue();
    }

    @Override
    public BoundZSetOperations<String, String> boundZSetOps(String key) {
        return super.boundZSetOps(key);
    }

    @Override
    public ZSetOperations<String, String> opsForZSet() {
        return super.opsForZSet();
    }

    @Override
    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        super.setEnableTransactionSupport(enableTransactionSupport);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        super.setBeanClassLoader(classLoader);
    }

    @Override
    public RedisConnectionFactory getConnectionFactory() {
        return super.getConnectionFactory();
    }

    @Override
    public RedisConnectionFactory getRequiredConnectionFactory() {
        return super.getRequiredConnectionFactory();
    }

    @Override
    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        super.setConnectionFactory(connectionFactory);
    }

    @Override
    public void restore(String key, byte[] value, long timeToLive, TimeUnit unit) {

    }

    //
    //                                  自定义方法
    // =====================================================================================

    public void setRedisTemplates(Map<Object, StringRedisTemplate> redisTemplates) {
        this.redisTemplates = redisTemplates;
    }

    public void setDefaultRedisTemplate(StringRedisTemplate defaultRedisTemplate) {
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
     * 动态redisTemplate
     */
    private StringRedisTemplate determineTargetRedisTemplate() {
        Integer database = RedisShardingThreadLocal.get();
        if (database == null) {
            return this.defaultRedisTemplate;
        }
        StringRedisTemplate redisTemplate = redisTemplates.get(database);
        if (redisTemplate == null) {
            redisTemplate = createRedisTemplateOnMissing(database);
            redisTemplates.put(database, redisTemplate);
        }
        return redisTemplate;
    }

    private StringRedisTemplate createRedisTemplateOnMissing(Integer database) {
        return null;
    }
}
