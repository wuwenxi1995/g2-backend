package org.g2.starter.redis.infra.hepler;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wuwenxi 2021-09-15
 */
public class RedisShardingThreadLocal {

    private RedisShardingThreadLocal() {
    }

    private static ThreadLocal<Deque<Integer>> THREAD_DB = new ThreadLocal<>();

    /**
     * 更新当前线程redis 客户端db
     *
     * @param database set current redis db
     */
    public static void set(int database) {
        Deque<Integer> deque = THREAD_DB.get();
        if (deque == null) {
            deque = new ArrayDeque<>();
        }
        deque.addFirst(database);
        THREAD_DB.set(deque);
    }

    /**
     * 获取当前线程redis 客户端db
     *
     * @return get current redis db
     */
    public static Integer get() {
        Deque<Integer> deque = THREAD_DB.get();
        if (deque == null) {
            return null;
        }
        return deque.getFirst();
    }

    /**
     * 清理
     */
    public static void clear() {
        Deque<Integer> deque = THREAD_DB.get();
        if (deque == null || deque.size() <= 1) {
            THREAD_DB.remove();
            return;
        }
        deque.removeFirst();
    }
}
