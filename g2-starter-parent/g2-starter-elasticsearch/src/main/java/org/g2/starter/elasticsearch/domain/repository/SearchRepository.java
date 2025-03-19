package org.g2.starter.elasticsearch.domain.repository;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.g2.starter.elasticsearch.domain.entity.BaseEntity;
import org.g2.starter.elasticsearch.domain.entity.Page;

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
     * <p>
     * 适合数据量不大的浅分页, 深度分页情况下性能差,
     * </p>
     *
     * @param searchSourceBuilder 聚合查询条件
     * @param page                分页
     * @param size                分页大小
     * @return 分页数据
     */
    Page<T> search(SearchSourceBuilder searchSourceBuilder, int page, int size);

    /**
     * 分页查询数据
     * <p>
     * 游标查询,滚屏翻页,不适合实时搜索和跳页需求
     * </p>
     *
     * @param sourceBuilder 聚合查询条件
     * @param scrollId      分页记录
     * @return 分页数据
     */
    Page<T> searchScroll(SearchSourceBuilder sourceBuilder, String scrollId);

    /**
     * 分页查询数据
     * <p>
     * 根据上一页的最后一条数据来确定下一页的位置,每一页的数据依赖于上一页最后一条数据,所以无法跳页请求，无法指定页数
     * </p>
     *
     * @param sourceBuilder 聚合查询条件
     * @param sortSources   上一页最后一条数据的排序结果
     * @param size          分页大小
     * @return 分页数据
     */
    Page<T> searchAfter(SearchSourceBuilder sourceBuilder, Object[] sortSources, int size);
}
