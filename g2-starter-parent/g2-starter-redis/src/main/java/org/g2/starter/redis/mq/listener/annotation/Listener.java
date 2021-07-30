package org.g2.starter.redis.mq.listener.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis 监听过期事件
 *
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Listener {

    /**
     * 监听redis库，默认-1为全部库
     *
     * @return db
     */
    int db() default -1;
}
