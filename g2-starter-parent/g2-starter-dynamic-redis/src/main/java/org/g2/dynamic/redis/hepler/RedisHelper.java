package org.g2.dynamic.redis.hepler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis操作工具类
 *
 * @author wuwenxi 2020-11-10
 */
public class RedisHelper implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RedisHelper.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Date.class, new DateSerializer());
        javaTimeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(redisTemplate, "redisTemplate must not be null");
    }

    public void setCurrentDataBase(int index) {
        logger.warn("Using Default RedisHelper, you'd batter use a dynamic/sharding RedisHelper instead.");
    }

    public void clearCurrentDataBase() {
        logger.warn("Using Default RedisHelper, you'd batter use a dynamic/sharding RedisHelper instead.");
    }

    public byte[] serialize(String s) {
        return getRedisTemplate().getStringSerializer().serialize(s);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * 默认过期时长，单位：秒
     */
    private static final long DEFAULT_EXPIRE = 60 * 60 * 24L;

    /**
     * 不设置过期时长
     */
    private static final long NOT_EXPIRE = -1;

    /**
     * 删除key
     *
     * @param key key
     */
    public void delKey(String key) {
        getRedisTemplate().delete(key);
    }

    /**
     * 删除key
     */
    public Boolean hasKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * returns -2 if the key does not exist.
     * returns -1 if the key exists but has no associated expire.
     *
     * @return TTL in seconds, or a negative value in order to signal an error
     */
    public Long getExpire(String key) {
        return getRedisTemplate().getExpire(key);
    }

    /**
     * returns -2 if the key does not exist.
     * returns -1 if the key exists but has no associated expire.
     *
     * @return TTL in seconds, or a negative value in order to signal an error
     */
    public Long getExpire(String key, final TimeUnit timeUnit) {
        return getRedisTemplate().getExpire(key, timeUnit);
    }

    /**
     * 设置过期时间,默认一天
     */
    public Boolean setExpire(String key) {
        return this.setExpire(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间,默认时间单位:秒
     *
     * @param key    key
     * @param expire 存活时长
     */
    public Boolean setExpire(String key, long expire) {
        return this.setExpire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     */
    public Boolean setExpire(String key, long expire, TimeUnit timeUnit) {
        return this.getRedisTemplate().expire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
    }

    /**
     * 批量删除Key
     */
    public void delKeys(Collection<String> keys) {
        Set<String> hs = new HashSet<>(keys);
        getRedisTemplate().delete(hs);
    }

    /**
     * String 设置值
     */
    public void strSet(String key, String value, long expire, TimeUnit timeUnit) {
        getRedisTemplate().opsForValue().set(key, value);
        if (expire != NOT_EXPIRE) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }
    }

    /**
     * String 设置值
     */
    public void strSet(String key, String value) {
        getRedisTemplate().opsForValue().set(key, value);
    }

    /**
     * String 获取值
     */
    public String strGet(String key) {
        return getRedisTemplate().opsForValue().get(key);
    }

    /**
     * String 获取值
     */
    public String strGet(String key, long expire, TimeUnit timeUnit) {
        String value = getRedisTemplate().opsForValue().get(key);
        if (expire != NOT_EXPIRE) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }
        return value;
    }

    /**
     * String 获取值
     */
    public <T> T strGet(String key, Class<T> clazz) {
        String value = getRedisTemplate().opsForValue().get(key);
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * String 设置值
     */
    public <T> T strGet(String key, Class<T> clazz, long expire, TimeUnit timeUnit) {
        String value = getRedisTemplate().opsForValue().get(key);
        if (expire != NOT_EXPIRE) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * String 获取值
     */
    public String strGet(String key, Long start, Long end) {
        return getRedisTemplate().opsForValue().get(key, start, end);
    }

    /**
     * 如果值不存在则设置（原子操作）
     */
    public Boolean strSetIfAbsent(String key, String value) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value);
    }

    /**
     * String 获取自增字段，递减字段可使用delta为负数的方式
     */
    public Long strIncrement(String key, Long delta) {
        return getRedisTemplate().opsForValue().increment(key, delta);
    }

    /**
     * List 推入数据至列表左端
     */
    public Long lstLeftPush(String key, String value) {
        return getRedisTemplate().opsForList().leftPush(key, value);
    }

    /**
     * List 推入数据至列表左端
     */
    public Long lstLeftPushAll(String key, Collection<String> values) {
        return getRedisTemplate().opsForList().leftPushAll(key, values);
    }

    /**
     * List 推入数据至列表右端
     */
    public Long lstRightPush(String key, String value) {
        return getRedisTemplate().opsForList().rightPush(key, value);
    }

    /**
     * List 推入数据至列表右端
     */
    public Long lstRightPushAll(String key, Collection<String> values) {
        return getRedisTemplate().opsForList().rightPushAll(key, values);
    }

    /**
     * List 返回列表键key中，从索引start至索引end范围的所有列表项。两个索引都可以是正数或负数
     */
    public List<String> lstRange(String key, long start, long end) {
        return getRedisTemplate().opsForList().range(key, start, end);
    }

    /**
     * List 返回列表键key中所有的元素
     */
    public List<String> lstAll(String key) {
        return this.lstRange(key, 0, this.lstLen(key));
    }

    /**
     * List 移除并返回列表最左端的项
     */
    public String lstLeftPop(String key) {
        return getRedisTemplate().opsForList().leftPop(key);
    }

    /**
     * List 移除并返回列表最右端的项
     */
    public String lstRightPop(String key) {
        return getRedisTemplate().opsForList().rightPop(key);
    }

    /**
     * List 移除并返回列表最左端的项
     */
    public String lstLeftPop(String key, long timeout, TimeUnit timeUnit) {
        return getRedisTemplate().opsForList().leftPop(key, timeout, timeUnit);
    }

    /**
     * List 移除并返回列表最右端的项
     */
    public String lstRightPop(String key, long timeout, TimeUnit timeUnit) {
        return getRedisTemplate().opsForList().rightPop(key, timeout, timeUnit);
    }

    /**
     * List 返回指定key的长度
     */
    public Long lstLen(String key) {
        return getRedisTemplate().opsForList().size(key);
    }

    /**
     * List 设置指定索引上的列表项。将列表键 key索引index上的列表项设置为value。 如果index参数超过了列表的索引范围，那么命令返回了一个错误
     */
    public void lstSet(String key, long index, String value) {
        getRedisTemplate().opsForList().set(key, index, value);
    }

    /**
     * List 根据参数 count的值，移除列表中与参数value相等的元素。 count的值可以是以下几种：count &gt; 0 :从表头开始向表尾搜索，移除与 value相等的元素，数量为
     * count
     */
    public Long lstRemove(String key, long index, String value) {
        return getRedisTemplate().opsForList().remove(key, index, value);
    }

    /**
     * List 返回列表键key中，指定索引index上的列表项。index索引可以是正数或者负数
     */
    public Object lstIndex(String key, long index) {
        return getRedisTemplate().opsForList().index(key, index);
    }

    /**
     * List 对一个列表进行修剪(trim)，让列表只保留指定索引范围内的列表项，而将不在范围内的其它列表项全部删除。 两个索引都可以是正数或者负数
     */
    public void lstTrim(String key, long start, long end) {
        getRedisTemplate().opsForList().trim(key, start, end);
    }

    /**
     * Set 将数组添加到给定的集合里面，已经存在于集合的元素会自动的被忽略， 命令返回新添加到集合的元素数量
     */
    public Long setAdd(String key, String[] values) {
        return getRedisTemplate().opsForSet().add(key, values);
    }

    /**
     * Set 将一个或多个元素添加到给定的集合里面，已经存在于集合的元素会自动的被忽略， 命令返回新添加到集合的元素数量
     */
    public Long setIrt(String key, String... values) {
        return getRedisTemplate().opsForSet().add(key, values);
    }

    /**
     * Set 将返回集合中所有的元素
     */
    public Set<String> setMembers(String key) {
        return getRedisTemplate().opsForSet().members(key);
    }

    /**
     * Set 检查给定的元素是否存在于集合
     */
    public Boolean setIsmember(String key, String o) {
        return getRedisTemplate().opsForSet().isMember(key, o);
    }

    /**
     * Set 返回集合包含的元素数量（也即是集合的基数）
     */
    public Long setSize(String key) {
        return getRedisTemplate().opsForSet().size(key);
    }

    /**
     * Set 计算所有给定集合的交集，并返回结果
     */
    public Set<String> setIntersect(String key, String otherKey) {
        return getRedisTemplate().opsForSet().intersect(key, otherKey);
    }

    /**
     * Set 计算所有的并集并返回结果
     */
    public Set<String> setUnion(String key, String otherKey) {
        return getRedisTemplate().opsForSet().union(key, otherKey);
    }

    /**
     * Set 计算所有的并集并返回结果
     */
    public Set<String> setUnion(String key, Collection<String> otherKeys) {
        return getRedisTemplate().opsForSet().union(key, otherKeys);
    }

    /**
     * Set 返回一个集合的全部成员，该集合是所有给定集合之间的差集
     */
    public Set<String> setDifference(String key, String otherKey) {
        return getRedisTemplate().opsForSet().difference(key, otherKey);
    }

    /**
     * Set 返回一个集合的全部成员，该集合是所有给定集合之间的差集
     */
    public Set<String> setDifference(String key, Collection<String> otherKeys) {
        return getRedisTemplate().opsForSet().difference(key, otherKeys);
    }

    /**
     * set 删除数据
     */
    public Long setDel(String key, String value) {
        return getRedisTemplate().opsForSet().remove(key, value);
    }

    /**
     * Set 批量删除数据
     */
    public Long setRemove(String key, Object[] value) {
        return getRedisTemplate().opsForSet().remove(key, value);
    }

    /**
     * ZSet Zadd 命令用于将一个或多个成员元素及其分数值加入到有序集当中。 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误
     */
    public Boolean zSetAdd(String key, String value, double score) {
        return getRedisTemplate().opsForZSet().add(key, value, score);
    }

    /**
     * ZSet 返回有序集合中，指定元素的分值
     */
    public Double zSetScore(String key, String value) {
        return getRedisTemplate().opsForZSet().score(key, value);
    }

    /**
     * ZSet 为有序集合指定元素的分值加上增量increment，命令返回执行操作之后，元素的分值 可以通过将 increment设置为负数来减少分值
     */
    public Double zSetIncrementScore(String key, String value, double delta) {
        return getRedisTemplate().opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * ZSet 返回指定元素在有序集合中的排名，其中排名按照元素的分值从小到大计算。排名以 0 开始
     */
    public Long zSetRank(String key, String value) {
        return getRedisTemplate().opsForZSet().rank(key, value);
    }

    /**
     * ZSet 返回成员在有序集合中的逆序排名，其中排名按照元素的分值从大到小计算
     */
    public Long zSetReverseRank(String key, String value) {
        return getRedisTemplate().opsForZSet().reverseRank(key, value);
    }

    /**
     * ZSet 返回有序集合的基数
     */
    public Long zSetSize(String key) {
        return getRedisTemplate().opsForZSet().size(key);
    }

    /**
     * ZSet 删除数据
     */
    public Long zSetRemove(String key, String value) {
        return getRedisTemplate().opsForZSet().remove(key, value);
    }

    /**
     * ZSet 根据score区间删除数据
     */
    public Long zSetRemoveByScore(String key, double min, double max) {
        return getRedisTemplate().opsForZSet().removeRangeByScore(key, min, max);
    }


    /**
     * ZSet 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。 具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     */
    public Set<String> zSetRange(String key, Long start, Long end) {
        return getRedisTemplate().opsForZSet().range(key, start, end);
    }

    /**
     * ZSet
     */
    public Set<String> zSetReverseRange(String key, Long start, Long end) {
        return getRedisTemplate().opsForZSet().reverseRange(key, start, end);
    }

    /**
     * ZSet 返回有序集合在按照分值升序排列元素的情况下，分值在 min 和 max范围之内的所有元素
     */
    public Set<String> zSetRangeByScore(String key, Double min, Double max) {
        return getRedisTemplate().opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * ZSet 返回有序集合在按照分值降序排列元素的情况下，分值在 min 和 max范围之内的所有元素
     */
    public Set<String> zSetReverseRangeByScore(String key, Double min, Double max) {
        return getRedisTemplate().opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * ZSet 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从小到大)的次序排列。 具有相同分数值的成员按字典序的顺序(reverse lexicographical order
     */
    public Set<String> zSetRangeByScore(String key, Double min, Double max, Long offset, Long count) {
        return getRedisTemplate().opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列
     */
    public Set<String> zSetReverseRangeByScore(String key, Double min, Double max, Long offset, Long count) {
        return getRedisTemplate().opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * ZSet 返回有序集合在升序排列元素的情况下，分值在 min和 max范围内的元素数量
     */
    public Long zSetCount(String key, Double min, Double max) {
        return getRedisTemplate().opsForZSet().count(key, min, max);
    }

    /**
     * Hash 将哈希表 key 中的域 field的值设为 value。如果 key不存在，一个新的哈希表被创建并进行HSET操作。 如果域 field已经存在于哈希表中，旧值将被覆盖
     */
    public void hshPut(String key, String hashKey, String value) {
        getRedisTemplate().opsForHash().put(key, hashKey, value);
    }

    /**
     * Hash 批量插入值，Map的key代表Field
     */
    public void hshPutAll(String key, Map<String, String> map) {
        getRedisTemplate().opsForHash().putAll(key, map);
    }

    /**
     * 获取hash对象中的对象序列字符
     */
    public byte[] hshGetSerial(String key, String hashKey) {
        RedisSerializer<String> redisSerializer = getRedisTemplate().getStringSerializer();
        return getRedisTemplate().execute((RedisCallback<byte[]>) connection -> {
            try {
                return connection.hGet(redisSerializer.serialize(key), redisSerializer.serialize(hashKey));
            } catch (Exception e) {
                logger.error("获取HASH对象序列失败", e);
            }
            return null;
        });
    }

    /**
     * 插入hash对象序列值
     */
    public Boolean hshPutSerial(String key, String hashKey, byte[] value) {
        RedisSerializer<String> redisSerializer = getRedisTemplate().getStringSerializer();
        return getRedisTemplate().execute((RedisCallback<Boolean>) connection -> {
            try {
                return connection.hSet(redisSerializer.serialize(key), redisSerializer.serialize(hashKey),
                        value);
            } catch (Exception e) {
                logger.error("插入HASH对象序列失败", e);
            }
            return Boolean.FALSE;
        });
    }

    /**
     * Hash 返回哈希表 key 中给定域 field的值，返回值：给定域的值。当给定域不存在或是给定 key不存在时，返回 nil
     */
    public String hshGet(String key, String hashKey) {
        return (String) getRedisTemplate().opsForHash().get(key, hashKey);
    }

    /**
     * Hash 返回散列键 key 中，一个或多个域的值，相当于同时执行多个 HGET
     */
    public List<String> hshMultiGet(String key, Collection<String> hashKeys) {
        Collection<Object> list = new ArrayList<>(hashKeys);
        List<Object> ret = getRedisTemplate().opsForHash().multiGet(key, list);
        return ret.stream().map(o -> (String) o).collect(Collectors.toList());
    }

    /**
     * Hash 获取散列Key中所有的键值对
     */
    public Map<String, String> hshGetAll(String key) {
        Map<Object, Object> map = getRedisTemplate().opsForHash().entries(key);
        Map<String, String> ret = new LinkedHashMap<>();
        map.forEach((k, v) -> ret.put((String) k, (String) v));
        return ret;
    }

    /**
     * Hash 查看哈希表 key 中，给定域 field是否存在
     */
    public Boolean hshHasKey(String key, String hashKey) {
        return getRedisTemplate().opsForHash().hasKey(key, hashKey);
    }

    /**
     * Hash 返回哈希表 key 中的所有域
     */
    public Set<String> hshKeys(String key) {
        Set<Object> set = getRedisTemplate().opsForHash().keys(key);
        return set.stream().map(o -> (String) o).collect(Collectors.toSet());
    }

    /**
     * Hash 返回散列键 key 中，所有域的值
     */
    public List<String> hshVals(String key) {
        List<Object> list = getRedisTemplate().opsForHash().values(key);
        return list.stream().map(o -> (String) o).collect(Collectors.toList());
    }

    /**
     * Hash 返回散列键 key中指定Field的域的值
     */
    public List<String> hshVals(String key, Collection<String> hashKeys) {
        Collection<Object> list = new ArrayList<>(hashKeys);
        List<Object> ret = getRedisTemplate().opsForHash().multiGet(key, list);
        return ret.stream().map(o -> (String) o).collect(Collectors.toList());
    }

    /**
     * Hash 散列键 key的数量
     */
    public Long hshSize(String key) {
        return getRedisTemplate().opsForHash().size(key);
    }

    /**
     * Hash 删除散列键 key 中的一个或多个指定域，以及那些域的值。不存在的域将被忽略。命令返回被成功删除的域值对数量
     */
    public void hshDelete(String key, Object... hashKeys) {
        getRedisTemplate().opsForHash().delete(key, hashKeys);
    }

    /**
     * Hash 删除散列键 key的数组
     */
    public void hshRemove(String key, Object[] hashKeys) {
        getRedisTemplate().opsForHash().delete(key, hashKeys);
    }

    /**
     * Object转成JSON数据
     */
    public <T> String toJson(T object) {
        if (object == null) {
            return StringUtils.EMPTY;
        }
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
                || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * JSON数据，转成Object
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || clazz == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }
    }

    /**
     * JSON数据，转成 List&lt;Object&gt;
     */
    public <T> List<T> fromJsonList(String json, Class<T> clazz) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * 将对象直接以json数据不设置过期时间的方式保存
     */
    public <T> void objectSet(String key, T object) {
        this.strSet(key, this.toJson(object));
    }
}
