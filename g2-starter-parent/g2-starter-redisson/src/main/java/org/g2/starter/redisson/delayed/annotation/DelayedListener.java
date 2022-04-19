package org.g2.starter.redisson.delayed.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列监听
 *
 * @author wuwenxi 2021-12-27
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
public @interface DelayedListener {

    String queue();

    int delayed() default 30;

    TimeUnit timeUnit() default TimeUnit.MINUTES;

    String executor() default "";
}
