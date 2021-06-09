package org.g2.starter.elasticsearch.infra.annotation;

import org.g2.starter.elasticsearch.infra.enums.Analyzer;
import org.g2.starter.elasticsearch.infra.enums.FieldType;

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
    FieldType type();

    /**
     * 是否允许被搜索
     */
    boolean index() default true;

    /**
     * 分词器
     * <p>
     * 当字段类型为{@link FieldType#TEXT}时应指定分词器，
     * 应用在建立索引和搜索时对文本进行分词，
     * 搜索时的分词器也可通过{@link Field#searchAnalyzer()}指定
     * </p>
     */
    Analyzer analyzer() default Analyzer.DEFAULT;

    /**
     * 查询分词器
     * <p>
     * 应用于搜索时对文本进行分词，默认搜索时的分词器与{@link Field#analyzer()}指定的相同，
     * 该参数可指定与{@link Field#analyzer()}不同的分词规则
     * </p>
     */
    Analyzer searchAnalyzer() default Analyzer.DEFAULT;

    /**
     * 是否fields字段
     */
    boolean fields() default false;

    /**
     * fields名称
     */
    String fieldsName() default "";
}
