package org.g2.starter.elasticsearch.app.serivce;

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
}
