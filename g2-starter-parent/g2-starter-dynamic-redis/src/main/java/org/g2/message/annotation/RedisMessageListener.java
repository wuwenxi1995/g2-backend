package org.g2.message.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wuwenxi 2022-12-06
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface RedisMessageListener {

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
