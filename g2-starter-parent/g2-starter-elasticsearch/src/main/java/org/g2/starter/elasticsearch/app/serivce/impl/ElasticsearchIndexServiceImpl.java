package org.g2.starter.elasticsearch.app.serivce.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.g2.core.exception.CommonException;
import org.g2.core.helper.ApplicationContextHelper;
import org.g2.core.util.StringUtil;
import org.g2.starter.elasticsearch.app.serivce.ElasticsearchIndexService;
import org.g2.starter.elasticsearch.config.ElasticsearchProperties;
import org.g2.starter.elasticsearch.config.spring.builder.RestClientBuildFactory;
import org.g2.starter.elasticsearch.infra.annotation.Document;
import org.g2.starter.elasticsearch.infra.annotation.Field;
import org.g2.starter.elasticsearch.infra.enums.Analyzer;
import org.g2.starter.elasticsearch.infra.enums.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-04-25
 */
@Service
public class ElasticsearchIndexServiceImpl implements ElasticsearchIndexService {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexServiceImpl.class);

    private final String token;
    private final RestClientBuildFactory restClientBuildFactory;

    private ElasticsearchIndexServiceImpl(RestClientBuildFactory restClientBuildFactory) {
        this.restClientBuildFactory = restClientBuildFactory;
        this.token = getToken(restClientBuildFactory.getProperties());
    }

    @Override
    public void reIndex(String token, String indexName) {
        if (!Objects.equals(this.token, token)) {
            throw new CommonException("invalid token !!!");
        }
        Map<String, Object> beans = ApplicationContextHelper.getApplicationContext().getBeansWithAnnotation(Document.class);
        if (StringUtils.isBlank(indexName)) {
            beans.values().forEach(bean -> this.createIndex(bean, false));
        } else {
            Map<String, Object> indexMaps = beans.values().stream().collect(Collectors.toMap(e -> e.getClass().getAnnotation(Document.class).indexName(), e -> e));
            if (!indexMaps.containsKey(indexName)) {
                throw new CommonException("");
            }
            createIndex(indexMaps.get(indexName), false);
        }
    }

    @Override
    public void createIndex(Object bean, boolean init) {
        Document document = bean.getClass().getAnnotation(Document.class);
        String indexName = document.indexName();
        GetIndexRequest request = new GetIndexRequest(indexName);
        boolean exists;
        try {
            exists = restClientBuildFactory.restHighLevelClient().indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Elasticsearch init error :", e);
            return;
        }
        // 初始化、需要重建索引，并且存在索引，先删除索引，再重建索引
        // 初始化、不需要重建索引，如果不存在索引，创建索引
        // 非初始化，存在索引，先删除索引，再重建索引
        // 非初始化，不存在索引，创建索引
        if ((!init || restClientBuildFactory.getProperties().isReIndex()) && exists) {
            // 先删除索引再重建索引
            doCreateIndexBefore(bean, 0);
        } else if (!exists) {
            doCreateIndex(bean);
        }
    }

    private void doCreateIndexBefore(Object bean, int index) {
        if (index > restClientBuildFactory.getProperties().getRetry()) {
            return;
        }
        String indexName = bean.getClass().getAnnotation(Document.class).indexName();
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
        restClientBuildFactory.restHighLevelClient().indices().deleteAsync(deleteRequest, RequestOptions.DEFAULT, new ActionListener<AcknowledgedResponse>() {
            @Override
            public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                log.info("delete index={} success in Elasticsearch", indexName);
                doCreateIndex(bean);
            }

            @Override
            public void onFailure(Exception e) {
                log.info("delete index={} fail in Elasticsearch, retry {}", indexName, index);
                int retry = index + 1;
                doCreateIndexBefore(bean, retry);
            }
        });
    }

    private void doCreateIndex(Object bean) {
        Document document = bean.getClass().getAnnotation(Document.class);
        String indexName = document.indexName();
        java.lang.reflect.Field[] fields = bean.getClass().getDeclaredFields();
        if (fields.length < 1) {
            return;
        }
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        // 设置分片与副本数量
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", document.shards())
                .put("index.number_of_replicas", document.replicas())
                .put("index.max_result_window", document.maxResult()));
        //封装属性 类似于json格式
        Map<String, Object> jsonMap = new HashMap<>(fields.length << 1);
        // 关闭动态mapping
        jsonMap.put("dynamic", false);
        // 创建mapping
        createMapping(jsonMap, fields, indexName);
        createIndexRequest.mapping(jsonMap);
        // 异步创建
        restClientBuildFactory.restHighLevelClient().indices().createAsync(createIndexRequest, RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                //如果执行成功，则调用onResponse方法;
                log.info("create index={} success in Elasticsearch", indexName);
            }

            @Override
            public void onFailure(Exception e) {
                //如果失败，则调用onFailure方法。
                log.error("create index={} fail in Elasticsearch", indexName);
            }
        });
    }

    private void createMapping(Map<String, Object> jsonMap, java.lang.reflect.Field[] fields, String indexName) {
        Map<String, Object> properties = new HashMap<>(16);
        for (java.lang.reflect.Field field : fields) {
            Field fieldAnnotation = field.getAnnotation(Field.class);
            if (null == fieldAnnotation) {
                log.warn("current es index ({}), the field ({}) has no @Field Tag", indexName, JSON.toJSONString(field));
                continue;
            }
            Map<String, Object> fieldMap = new HashMap<>(16);
            fieldMap.put("type", fieldAnnotation.type());
            // fix by wwx 修改嵌套查询类型
            if (Objects.equals(FieldType.NESTED, fieldAnnotation.type())) {
                Type type = field.getGenericType();
                String className;
                if (type instanceof ParameterizedType) {
                    className = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
                } else {
                    className = type.getTypeName();
                }
                Class<?> aClass;
                try {
                    aClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    log.error("es init mapping for [nested] error, msg: {}", StringUtil.exceptionString(e));
                    continue;
                }
                log.info("es init, field type nested and class: {}", aClass);
                java.lang.reflect.Field[] declaredFields = aClass.getDeclaredFields();
                if (declaredFields.length > 1) {
                    createMapping(fieldMap, declaredFields, indexName);
                }
            }
            if (FieldType.DATE.equals(fieldAnnotation.type())) {
                properties.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
                // 格式错误的数字将引发异常并拒绝整个文档
                properties.put("ignore_malformed", false);
                // 默认为null，这意味着该字段被视为丢失
                properties.put("null_value", null);
            }
            if (!fieldAnnotation.index()) {
                properties.put("index", fieldAnnotation.index());
            } else if (FieldType.TEXT.equals(fieldAnnotation.type())) {
                Analyzer analyzer = Analyzer.DEFAULT.equals(fieldAnnotation.analyzer()) ? Analyzer.IK_MAX_WORD : fieldAnnotation.analyzer();
                properties.put("analyzer", analyzer.getAnalyzer());
                if (StringUtils.isNotBlank(fieldAnnotation.searchAnalyzer().getAnalyzer())) {
                    properties.put("search_analyzer", fieldAnnotation.searchAnalyzer().getAnalyzer());
                }
            }
            properties.put(field.getName(), fieldMap);
        }
        jsonMap.put("properties", properties);
    }

    private String getToken(ElasticsearchProperties properties) {
        return String.format("%s-%s", properties.getUsername(), properties.getPassword());
    }
}
