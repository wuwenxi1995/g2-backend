package org.g2.starter.elasticsearch.infra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * es 文档字段类型
 *
 * @author wenxi.wu@hand-china.com 2020-09-23
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 多重注解
@Repeatable(Fields.class)
public @interface Field {

    /**
     * 类型
     */
    String type();

    /**
     * 是否走索引
     */
    boolean index() default true;

    /**
     * 分词器
     */
    String analyzer() default "";

    /**
     * 查询分词器
     */
    String searchAnalyzer() default "";

    /**
     * 是否fields字段
     */
    boolean fields() default false;

    /**
     * fields名称
     */
    String fieldsName() default "";
}
