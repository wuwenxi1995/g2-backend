package org.g2.dynamic.redis.util;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wuwenxi 2021-12-07
 */
public class RedisShardingTheadLocal {

    private RedisShardingTheadLocal() {
    }

    private static ThreadLocal<Deque<String>> THREAD_SHARDING_NAME = new ThreadLocal<>();

    /**
     * 给当前线程添加一个分片
     */
    public static void set(String shardingName) {
        Deque<String> deque = THREAD_SHARDING_NAME.get();
        if (deque == null) {
            deque = new ArrayDeque<>();
        }
        deque.addFirst(shardingName);
        THREAD_SHARDING_NAME.set(deque);
    }

    /**
     * 获取当前线程第一个分片
     */
    public static String get() {
        Deque<String> deque = THREAD_SHARDING_NAME.get();
        if (deque == null) {
            return null;
        }
        return deque.getFirst();
    }

    /**
     * 如果当前现在有多个分片，依次进行清理
     */
    public static void clear() {
        Deque<String> deque = THREAD_SHARDING_NAME.get();
        if (deque == null || deque.size() <= 1) {
            THREAD_SHARDING_NAME.remove();
            return;
        }
        deque.removeFirst();
        THREAD_SHARDING_NAME.set(deque);
    }
}
