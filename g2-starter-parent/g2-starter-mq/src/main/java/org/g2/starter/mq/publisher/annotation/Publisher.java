package org.g2.starter.mq.publisher.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis pub/sub 发布者
 *
 * @author wuwenxi 2021-05-21
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Publisher {
}
