package org.g2.starter.elasticsearch.infra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.g2.starter.elasticsearch.infra.constants.OmsElasticsearchConstants;
import org.springframework.stereotype.Component;


/**
 * es Document 注解
 *
 * @author wenxi.wu@hand-china.com 2020-09-23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Document {
    /**
     * 索引
     */
    String indexName();

    /**
     * 类型
     */
    String type() default OmsElasticsearchConstants.IndexType.DEFAULT;

    /**
     * 分片数
     */
    int shards() default 1;

    /**
     * 副本数
     */
    int replicas() default 1;

    /**
     * 分页最大返回数
     */
    long maxResult() default 10000;
}
