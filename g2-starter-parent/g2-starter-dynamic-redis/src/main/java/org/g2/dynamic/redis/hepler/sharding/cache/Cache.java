package org.g2.dynamic.redis.hepler.sharding.cache;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author wuwenxi 2022-04-19
 */
public class Cache<T> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private AtomicReferenceArray<T> table = null;
    private final int tableSize;

    public Cache() {
        this.tableSize = DEFAULT_INITIAL_CAPACITY;
    }

    public Cache(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        this.tableSize = tableSizeFor(initialCapacity);
    }

    public T put(Object key, T data) {
        if (table == null) {
            initTable();
        }
        int i, hash = hash(key);
        T p;
        if ((p = table.get((i = (table.length() - 1) & hash))) == null) {
            while (table.get(i) == null) {
                if (table.compareAndSet(i, null, data)) {
                    break;
                }
            }
            p = table.get(i);
        }
        return p;
    }

    public T get(Object key) {
        if (table == null) {
            return null;
        }
        T p;
        return (p = table.get((table.length() - 1) & hash(key))) == null ? null : p;
    }

    private int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int tableSizeFor(int size) {
        int n = size - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    private synchronized void initTable() {
        if (table == null) {
            this.table = new AtomicReferenceArray<>(tableSize);
        }
    }
}