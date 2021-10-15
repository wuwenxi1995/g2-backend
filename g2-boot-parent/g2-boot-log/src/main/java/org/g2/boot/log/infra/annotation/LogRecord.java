package org.g2.boot.log.infra.annotation;

import org.g2.boot.log.infra.enums.LogRecordType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解允许标准在类和方法上
 * <p>
 * 标注的方法必须是公有方法
 * </p>
 *
 * @author wuwenxi 2021-10-13
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface LogRecord {

    LogRecordType type();

    String code();

    String name();
}
