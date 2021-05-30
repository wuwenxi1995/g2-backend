package org.g2.boot.elasticsearch.infra.repository.impl;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetFieldMappingsRequest;
import org.elasticsearch.client.indices.GetFieldMappingsResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.g2.boot.elasticsearch.domain.entity.Page;
import org.g2.boot.elasticsearch.domain.repository.BaseRepository;
import org.g2.boot.elasticsearch.infra.uitl.ElasticsearchPageUtil;
import org.g2.core.helper.FastJsonHelper;
import org.g2.core.util.Reflections;
import org.g2.core.util.StringUtil;
import org.g2.starter.elasticsearch.config.spring.builder.RestClientBuildFactory;
import org.g2.starter.elasticsearch.domain.entity.BaseEntity;
import org.g2.starter.elasticsearch.infra.annotation.Document;
import org.g2.starter.elasticsearch.infra.constants.OmsElasticsearchConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
public class BaseRepositoryImpl<T extends BaseEntity> implements BaseRepository<T> {

    private static final Logger log = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    @Autowired
    private RestClientBuildFactory restClientBuildFactory;

    private String indexName;
    private Class<T> tClass;

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        Class<T> tClass = (Class<T>) Reflections.getClassGenericType(this.getClass());
        this.tClass = tClass;
        Document annotation = tClass.getAnnotation(Document.class);
        if (null != annotation) {
            indexName = annotation.indexName();
        }

    }

    //
    //                  implements ElasticsearchBaseRepository
    // -------------------------------------------------------------------------

    @Override
    public void createIndex(CreateIndexRequest createIndexRequest) throws IOException {
        try {
            restClientBuildFactory.restHighLevelClient().indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("elasticsearch create index error : {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void createIndex(String indexName) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
                .put("index.max_result_window", 10000));
        this.createIndex(createIndexRequest);
    }

    @Override
    public boolean existsIndex(String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        try {
            return restClientBuildFactory.restHighLevelClient().indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("elasticsearch exists index error : {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        try {
            AcknowledgedResponse response = restClientBuildFactory.restHighLevelClient().indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("elasticsearch delete index error : {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean createMapping(String indexName, Map<String, String> mappings) throws IOException {
        Map<String, Object> mapping = new HashMap<>(16);
        Map<String, Object> properties = new HashMap<>(16);
        mappings.forEach((key, value) -> {
            // 字段
            Map<String, Object> fieldMap = new HashMap<>(4);
            // 字段属性
            fieldMap.put("type", value);
            fieldMap.put("index", true);
            if (OmsElasticsearchConstants.FiledType.TEXT.equals(value)) {
                fieldMap.put("analyzer", OmsElasticsearchConstants.Analyzer.IK_MAX_WORD);
                fieldMap.put("search_analyzer", OmsElasticsearchConstants.Analyzer.IK_SMART);
            }
            if (OmsElasticsearchConstants.FiledType.DATE.equals(value)) {
                fieldMap.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
                // 格式错误的数字将引发异常并拒绝整个文档
                fieldMap.put("ignore_malformed", false);
                // 默认为null，这意味着该字段被视为丢失
                fieldMap.put("null_value", null);
            }
            properties.put(key, fieldMap);
        });
        mapping.put("properties", properties);

        // 创建映射
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
        putMappingRequest.source(mapping);

        try {
            AcknowledgedResponse response = restClientBuildFactory.restHighLevelClient().indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("elasticsearch put mapping error : {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Object> searchMappings(String indexName) throws IOException {
        GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
        getMappingsRequest.indices(indexName);
        try {
            GetMappingsResponse mapping = restClientBuildFactory.restHighLevelClient().indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
            Map<String, MappingMetaData> mappings = mapping.mappings();
            if (log.isDebugEnabled()) {
                log.debug("index [{}] mapping data :{}", indexName, FastJsonHelper.objectConvertString(mapping));
            }
            MappingMetaData mappingMetaData = mappings.get(indexName);
            return mappingMetaData.getSourceAsMap();
        } catch (IOException e) {
            log.error("elasticsearch get mapping error : {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Object> searchFiledMapping(String indexName, String filed) throws IOException {
        GetFieldMappingsRequest request = new GetFieldMappingsRequest();
        request.indices(indexName);
        request.fields(filed);
        try {
            GetFieldMappingsResponse response = restClientBuildFactory.restHighLevelClient().indices().getFieldMapping(request, RequestOptions.DEFAULT);
            Map<String, Map<String, GetFieldMappingsResponse.FieldMappingMetaData>> mappings = response.mappings();
            return mappings.get(indexName).get(filed).sourceAsMap();
        } catch (IOException e) {
            log.error("elasticsearch get filed mapping error : {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> analyze(String indexName, String analyzer, String... text) throws IOException {
        if (StringUtils.isBlank(analyzer)) {
            analyzer = OmsElasticsearchConstants.Analyzer.IK_MAX_WORD;
        }
        AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer(indexName, analyzer, text);
        try {
            AnalyzeResponse response = restClientBuildFactory.restHighLevelClient().indices().analyze(request, RequestOptions.DEFAULT);
            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
            return tokens.stream().map(AnalyzeResponse.AnalyzeToken::getTerm).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("elasticsearch analyze has error : {}", e.getMessage());
            throw e;
        }
    }


    //
    //                  implements BaseDocumentRepository
    // -------------------------------------------------------------------------

    @Override
    public void indexDocument(T document) {

    }

    @Override
    public boolean existsDocument(String id) {
        return false;
    }

    @Override
    public boolean existsDocument(T document) {
        return false;
    }

    @Override
    public void deleteDocument(String id) {

    }

    @Override
    public void deleteDocument(T document) {

    }

    @Override
    public void updateDocument(T document) {

    }

    //
    //                  implements BulkRepository
    // -------------------------------------------------------------------------

    @Override
    public void batchIndexDocument(List<T> documents) {

    }

    @Override
    public void batchUpdateDocument(List<T> documents) {

    }

    @Override
    public void batchDeleteDocument(List<T> documents) {

    }

    @Override
    public BulkResponse bulk(BulkRequest bulkRequest) throws IOException {
        try {
            return restClientBuildFactory.restHighLevelClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("elasticsearch invoke bulk error : ", e);
            throw e;
        }
    }

    @Override
    public BulkProcessor initBulkProcessor(BulkProcessor.Listener listener) {
        listener = listener == null ? new CustomBulkProcessorListener() : listener;
        return BulkProcessor.builder(
                (request, bulkListener) ->
                        restClientBuildFactory.restHighLevelClient().bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener)
                // 根据当前添加的操作数设置何时刷新新的批量请求（默认为1000，使用-1禁用它）
                // 1000请求刷新一次bulk
                .setBulkActions(1000)
                // 根据当前添加的操作大小设置何时刷新新的批量请求（默认为5Mb，使用-1禁用它）
                // bulk数据10MB时刷新
                .setBulkSize(new ByteSizeValue(10L, ByteSizeUnit.MB))
                // 设置允许执行的并发请求数（默认为1，使用0仅允许执行单个请求）
                // 设置3个并发请求
                .setConcurrentRequests(2)
                // 设置刷新间隔，如果间隔过去，则刷新所有BulkRequest挂起的间隔（默认为未设置）
                // 5L 刷新一次
                .setFlushInterval(TimeValue.timeValueSeconds(5L))
                // 设置一个恒定的退避策略，该策略最初等待1秒，然后重试3次
                // 设置 等待0.1s
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }

    @Override
    public BulkProcessor initBulkProcessor() {
        return initBulkProcessor(new CustomBulkProcessorListener());
    }

    private static class CustomBulkProcessorListener implements BulkProcessor.Listener {

        @Override
        public void beforeBulk(long executionId, BulkRequest request) {
            int numberOfActions = request.numberOfActions();
            log.debug("Executing bulk [{}] with {} requests",
                    executionId, numberOfActions);
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request,
                              BulkResponse response) {
            if (response.hasFailures()) {
                log.warn("Bulk [{}] executed with failures", executionId);
            } else {
                log.debug("Bulk [{}] completed in {} milliseconds",
                        executionId, response.getTook().getMillis());
            }
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request,
                              Throwable failure) {
            log.error("Failed to execute bulk", failure);
        }
    }

    //
    //                  implements SearchRepository
    // -------------------------------------------------------------------------

    @Override
    public T search(String id) {
        GetRequest getRequest = new GetRequest(indexName, id);
        try {
            GetResponse response = restClientBuildFactory.restHighLevelClient().get(getRequest, RequestOptions.DEFAULT);
            if (response.isExists()) {
                if (log.isDebugEnabled()) {
                    log.debug("elasticsearch search response : {}", FastJsonHelper.objectConvertString(response));
                }
                String source = response.getSourceAsString();
                return FastJsonHelper.stringConvertObject(source, tClass);
            }
        } catch (IOException e) {
            log.error("elasticsearch getRequest error : ", e);
        }
        return null;
    }

    @Override
    public Page<T> search(SearchSourceBuilder searchSourceBuilder, int page, int size) {
        Page<T> result = new Page<>(page, size);
        ElasticsearchPageUtil.page(searchSourceBuilder, result);

        SearchResponse searchResponse;
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        searchRequest.source(searchSourceBuilder);
        try {
            searchResponse = restClientBuildFactory.restHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("elasticsearch search error : {}", StringUtil.exceptionString(e));
            return result;
        }
        SearchHits hits = searchResponse.getHits();
        result.setAggregations(searchResponse.getAggregations());
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        if (total == 0) {
            return result;
        }
        List<T> tList = new ArrayList<>();
        for (SearchHit searchHit : hits) {
            String source = searchHit.getSourceAsString();
            T entity = FastJsonHelper.stringConvertObject(source, tClass);
            entity.setId(searchHit.getId());
            tList.add(entity);
        }
        result.setData(tList);
        return result;
    }
}
