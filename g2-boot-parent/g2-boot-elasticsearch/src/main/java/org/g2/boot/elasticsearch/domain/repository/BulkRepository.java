package org.g2.boot.elasticsearch.domain.repository;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.g2.starter.elasticsearch.domain.entity.BaseEntity;

/**
 * es bulk java 文档 https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-bulk.html
 *
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public interface BulkRepository<T extends BaseEntity> {

    /**
     * 批量保存文档
     *
     * @param documents 文档
     */
    void batchIndexDocument(List<T> documents);

    /**
     * 批量更新文档
     *
     * @param documents 文档
     */
    void batchUpdateDocument(List<T> documents);

    /**
     * 批量删除文档
     *
     * @param documents 文档
     */
    void batchDeleteDocument(List<T> documents);

    /**
     * 使用bulk执行请求
     *
     * @param bulkRequest bulk请求
     * @return BulkResponse
     * @throws IOException 异常信息
     */
    BulkResponse bulk(BulkRequest bulkRequest) throws IOException;

    /**
     * 初始化BulkProcessor
     * <p>
     * BulkProcessor 多线程提交请求
     * 使用结束后 关闭BulkProcessor ==》bulkProcessor.close();
     *
     * @param listener 监听器
     * @return BulkProcessor
     */
    BulkProcessor initBulkProcessor(BulkProcessor.Listener listener);

    /**
     * 初始化BulkProcessor
     *
     * @return bulkProcessor
     */
    BulkProcessor initBulkProcessor();
}
