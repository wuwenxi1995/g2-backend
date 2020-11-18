package org.g2.boot.elasticsearch.domain.repository;

import org.g2.starter.elasticsearch.domain.entity.BaseEntity;

/**
 * es 文档 增删改
 *
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public interface BaseDocumentRepository<T extends BaseEntity> {

    /**
     * 保存文档数据
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-index.html
     *
     * @param document 文档数据
     */
    void indexDocument(T document);

    /**
     * 是否存在文档
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-exists.html
     *
     * @param id 文档id
     * @return true/false
     */
    boolean existsDocument(String id);

    /**
     * 是否存在文档
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-exists.html
     *
     * @param document 文档信息，包括索引、id信息
     * @return true/false
     */
    boolean existsDocument(T document);

    /**
     * 删除文档
     *
     * @param id 文档id
     */
    void deleteDocument(String id);

    /**
     * 删除文档
     *
     * @param document 文档信息，包括索引、id信息
     */
    void deleteDocument(T document);

    /**
     * 更新文档
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-update.html
     *
     * @param document 文档
     */
    void updateDocument(T document);
}
