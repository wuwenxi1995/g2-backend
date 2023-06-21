package org.g2.starter.dynamic.jdbc.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  动态切换数据源路由
 *
 * @author wuwenxi 2023-03-13
 */
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface DataRoute {

    String dsKey() default "NULL_KEY";
}
