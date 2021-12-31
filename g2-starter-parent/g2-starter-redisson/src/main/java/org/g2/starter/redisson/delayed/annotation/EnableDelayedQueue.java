package org.g2.starter.redisson.delayed.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用基于注解的延时队列
 *
 * @author wuwenxi 2021-12-27
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
@Import(DelayedQueueConfigurationSelector.class)
public @interface EnableDelayedQueue {
}
