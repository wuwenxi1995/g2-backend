package org.g2.boot.elasticsearch.domain.repository;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.g2.boot.elasticsearch.domain.entity.Page;
import org.g2.starter.elasticsearch.domain.entity.BaseEntity;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public interface SearchRepository<T extends BaseEntity> {

    /**
     * GetRequest 查询文档
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-get.html
     *
     * @param id 文档id
     * @return T 文档
     */
    T search(String id);

    /**
     * 分页查询数据
     *
     * @param searchSourceBuilder 聚合查询条件
     * @param page                分页
     * @param size                分页大小
     * @return 分页数据
     * @throws Exception 异常信息
     */
    Page<T> search(SearchSourceBuilder searchSourceBuilder, int page, int size) throws Exception;
}
