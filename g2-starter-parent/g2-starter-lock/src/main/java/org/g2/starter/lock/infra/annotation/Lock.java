package org.g2.starter.lock.infra.annotation;

import org.g2.starter.lock.infra.enums.LockType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    /**
     * 锁名称
     */
    @AliasFor(attribute = "name")
    String value() default "";

    /**
     * 锁名称
     */
    @AliasFor(attribute = "value")
    String name() default "";

    /**
     * 分布式锁类型
     *
     * @see LockType
     */
    LockType lockType() default LockType.FAIR;

    /**
     * 是否启用spel表达式，默认不启用
     */
    boolean spel() default false;

    /**
     * 获取分布式锁等待时间，默认为最小整数
     */
    long waitTime() default Integer.MIN_VALUE;

    /**
     * 分布式锁自动释放时间，默认为最小整数
     */
    long leaseTime() default Integer.MIN_VALUE;

    /**
     * timeUnit类型
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
