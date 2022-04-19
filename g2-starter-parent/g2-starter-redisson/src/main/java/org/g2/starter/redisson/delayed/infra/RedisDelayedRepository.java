package org.g2.starter.redisson.delayed.infra;

import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2022-04-07
 */
public interface RedisDelayedRepository<T> {

    boolean add(T data);

    boolean add(T data, int delayed, TimeUnit timeUnit);

    boolean remove(T data);
}
