package org.g2.starter.elasticsearch.domain.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.client.indices.CreateIndexRequest;
import org.g2.starter.elasticsearch.infra.constants.OmsElasticsearchConstants;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public interface ElasticsearchBaseRepository {

    /**
     * 创建索引
     *
     * @param createIndexRequest 索引内容
     * @throws IOException 异常信息
     */
    void createIndex(CreateIndexRequest createIndexRequest) throws IOException;

    /**
     * 创建索引
     *
     * @param indexName 索引名
     * @throws IOException 异常信息
     */
    void createIndex(String indexName) throws IOException;

    /**
     * 是否存在索引
     *
     * @param indexName 索引名
     * @return true/false
     * @throws IOException 异常信息
     */
    boolean existsIndex(String indexName) throws IOException;

    /**
     * 删除索引
     *
     * @param indexName 索引名
     * @throws IOException 异常信息
     */
    boolean deleteIndex(String indexName) throws IOException;

    /**
     * 创建索引
     *
     * @param indexName 索引名
     * @param mappings  映射关系
     * @throws IOException 异常信息
     */
    boolean createMapping(String indexName, Map<String, String> mappings) throws IOException;

    /**
     * 查询映射
     *
     * @param indexName 索引名
     * @return 索引中全部字段
     * @throws IOException 异常信息
     */
    Map<String, Object> searchMappings(String indexName) throws IOException;

    /**
     * 查询单个字段映射
     *
     * @param indexName 索引名
     * @param flied     字段
     * @return 字段信息
     * @throws IOException 异常信息
     */
    Map<String, Object> searchFiledMapping(String indexName, String flied) throws IOException;

    /**
     * 分词
     *
     * @param indexName 索引名
     * @param analyzer  分词器
     * @param text      内容
     * @return 分词结果
     * @throws IOException 异常信息
     */
    List<String> analyze(String indexName, String analyzer, String... text) throws IOException;

}
