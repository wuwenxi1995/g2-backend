package org.g2.starter.redisson.infra.annotation;

import org.g2.starter.redisson.infra.enums.LockType;
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

    @AliasFor(attribute = "name")
    String value() default "";

    @AliasFor(attribute = "value")
    String name() default "";

    LockType lockType() default LockType.FAIR;

    long waitTime() default Integer.MIN_VALUE;

    long leaseTime() default Integer.MIN_VALUE;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
