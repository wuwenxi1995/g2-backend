package org.g2.boot.elasticsearch.app.service;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public interface ElasticsearchService {

    /**
     * 重建索引
     *
     * @param indexName 索引名称
     */
    void reIndex(String indexName);
}
