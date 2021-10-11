package org.g2.starter.redisson.lock.infra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 基于zookeeper的分布式锁
 *
 * @author wuwenxi 2021-06-21
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Mutex {

    String path();

    long waitTime() default 10;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
