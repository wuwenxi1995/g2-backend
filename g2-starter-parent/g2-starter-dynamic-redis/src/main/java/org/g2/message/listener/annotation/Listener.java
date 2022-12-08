package org.g2.message.listener.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wuwenxi 2022-12-06
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Listener {

    /**
     * 队列名
     *
     * @return 队列ming
     */
    String queue();

    /**
     * redis库
     *
     * @return db
     */
    int db() default 0;
}
