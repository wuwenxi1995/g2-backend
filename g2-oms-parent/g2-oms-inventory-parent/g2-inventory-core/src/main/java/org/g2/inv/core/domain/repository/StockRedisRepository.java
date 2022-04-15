package org.g2.inv.core.domain.repository;

import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author wuwenxi 2022-04-15
 */
public interface StockRedisRepository {

    /**
     * 公共操作方法
     *
     * @param posCode  服务点编码
     * @param supplier 回调方法
     * @param <T>      返回参数类型
     * @return T
     */
    <T> T operation(String posCode, Supplier<T> supplier);

    /**
     * 执行脚本
     *
     * @param redisScript 脚本
     * @param key         key
     * @param params      参数
     * @param <T>         返回参数
     * @return T
     */
    <T> T execute(RedisScript<T> redisScript, List<String> key, Object... params);
}
