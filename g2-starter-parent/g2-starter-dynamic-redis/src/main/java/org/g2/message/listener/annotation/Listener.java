package org.g2.message.listener.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

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

    /**
     * 任务执行间隔
     */
    int delayed() default 1;

    /**
     * 任务执行时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否自动提交
     *
     * @return true/false
     */
    boolean isAutoCommit() default true;

    /**
     * 自动回滚时间, 分钟
     *
     * @return 自动回滚时间
     */
    int rollbackTimeM() default 5;

    /**
     * 执行异常, 重试次数
     *
     * @return retry
     */
    int retry() default 5;
}
