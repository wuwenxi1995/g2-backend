package org.g2.starter.elasticsearch.app.serivce;

import java.util.Map;

/**
 * @author wuwenxi 2022-04-25
 */
public interface ElasticsearchIndexService {
    /**
     * 重建索引
     *
     * @param token     身份验证信息
     * @param indexName 索引名
     */
    void reIndex(String token, String indexName);

    /**
     * 创建索引
     *
     * @param bean 包含document注解到bean
     * @param init 是否初始化
     */
    void createIndex(Object bean, boolean init);

    /**
     * 获取索引对象
     *
     * @param indexName 索引名
     * @return 索引对象
     */
    Object getDocument(String indexName);

    /**
     * 创建映射
     *
     * @param jsonMap   map
     * @param fields    索引对象字段
     * @param indexName 索引名
     */
    void createMapping(Map<String, Object> jsonMap, java.lang.reflect.Field[] fields, String indexName);
}
