package org.g2.autoconfigura.scheduler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.g2.autoconfigura.scheduler.config.EnableSchedulerAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author wenxi.wu@hand-china.com 2020-11-02
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EnableSchedulerAutoConfiguration.class})
public @interface EnableG2Scheduler {
}
