package org.g2.starter.redis.mq.subject.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis pub/sub 订阅者
 *
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Subject {

    /**
     * topic
     *
     * @return topic 名称
     */
    String[] values();
}
