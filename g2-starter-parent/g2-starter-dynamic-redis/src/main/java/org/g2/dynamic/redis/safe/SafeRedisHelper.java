package org.g2.dynamic.redis.safe;

import org.g2.core.helper.ApplicationContextHelper;
import org.g2.core.util.Operation;
import org.g2.dynamic.redis.hepler.RedisHelper;
import org.g2.dynamic.redis.hepler.sharding.ShardingRedisHelper;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @author wuwenxi 2021-12-08
 */
@Component
public class SafeRedisHelper {

    private static RedisHelper redisHelper;
    private static ShardingRedisHelper shardingRedisHelper;

    static {
        // 静态注入
        ApplicationContextHelper.asyncStaticSetter(RedisHelper.class, SafeRedisHelper.class, "setRedisHelper");
        ApplicationContextHelper.asyncStaticSetter(ShardingRedisHelper.class, SafeRedisHelper.class, "setShardingRedisHelper");
    }

    public static void setRedisHelper(RedisHelper redisHelper) {
        SafeRedisHelper.redisHelper = redisHelper;
    }

    public static void setShardingRedisHelper(ShardingRedisHelper shardingRedisHelper) {
        SafeRedisHelper.shardingRedisHelper = shardingRedisHelper;
    }

    //
    //                              SafeRedisHelper
    // =====================================================================

    public void execute(int db, RedisHelper redisHelper, Operation operation) {
        try {
            redisHelper.setCurrentDataBase(db);
            operation.operation();
        } finally {
            redisHelper.clearCurrentDataBase();
        }
    }

    public void execute(int db, Operation operation) {
        try {
            redisHelper.setCurrentDataBase(db);
            operation.operation();
        } finally {
            redisHelper.clearCurrentDataBase();
        }
    }

    public <T> T execute(int db, RedisHelper redisHelper, Supplier<T> supplier) {
        try {
            redisHelper.setCurrentDataBase(db);
            return supplier.get();
        } finally {
            redisHelper.clearCurrentDataBase();
        }
    }

    public <T> T execute(int db, Supplier<T> supplier) {
        try {
            redisHelper.setCurrentDataBase(db);
            return supplier.get();
        } finally {
            redisHelper.clearCurrentDataBase();
        }
    }

    //
    //                              SafeShardingRedisHelper
    // =====================================================================

    public void execute(Object sharding, int db, ShardingRedisHelper shardingRedisHelper, Operation operation) {
        try {
            shardingRedisHelper.setShardingName(sharding);
            shardingRedisHelper.setCurrentDataBase(db);
            operation.operation();
        } finally {
            shardingRedisHelper.clearShardingName();
            shardingRedisHelper.clearCurrentDataBase();
        }
    }

    public void execute(Object sharding, Operation operation) {
        try {
            shardingRedisHelper.setShardingName(sharding);
            operation.operation();
        } finally {
            shardingRedisHelper.clearShardingName();
        }
    }
}
