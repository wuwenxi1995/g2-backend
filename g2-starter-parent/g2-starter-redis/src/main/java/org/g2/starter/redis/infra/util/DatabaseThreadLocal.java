package org.g2.starter.redis.infra.util;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
public final class DatabaseThreadLocal {

    private static ThreadLocal<Integer> database = new ThreadLocal<>();

    private DatabaseThreadLocal() {
    }

    public static void set(Integer index) {
        database.set(index);
    }

    public static Integer get() {
        return database.get();
    }

    public static void clear() {
        database.remove();
    }
}
