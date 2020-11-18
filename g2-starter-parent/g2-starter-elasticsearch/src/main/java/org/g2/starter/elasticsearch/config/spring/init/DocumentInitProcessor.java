package org.g2.starter.elasticsearch.config.spring.init;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.g2.core.helper.FastJsonHelper;
import org.g2.starter.elasticsearch.infra.annotation.Document;
import org.g2.starter.elasticsearch.infra.annotation.Field;
import org.g2.starter.elasticsearch.infra.constants.OmsElasticsearchConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

/**
 * 拦截所有Document注解的组件创建
 * 初始化es 索引
 *
 * @author wenxi.wu@hand-china.com 2020-09-23
 */
public class DocumentInitProcessor implements BeanPostProcessor, BeanFactoryAware {

    private static final Logger log = LoggerFactory.getLogger(DocumentInitProcessor.class);

    private RestHighLevelClient restHighLevelClient;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        Document document = AnnotationUtils.getAnnotation(bean.getClass(), Document.class);
        java.lang.reflect.Field[] jdkFields = bean.getClass().getDeclaredFields();
        if (document != null && jdkFields.length > 0) {
            String indexName = document.indexName();

            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            try {
                boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
                if (!exists) {
                    CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                    // 设置分片与副本数量
                    createIndexRequest.settings(Settings.builder()
                            .put("index.number_of_shards", document.shards())
                            .put("index.number_of_replicas", document.replicas())
                            .put("index.max_result_window", document.maxResult()));
                    Map<String, Object> mapping = createMapping(jdkFields, indexName);
                    createIndexRequest.mapping(mapping);
                    // 异步创建索引
                    restHighLevelClient.indices().createAsync(createIndexRequest, RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {

                        @Override
                        public void onResponse(CreateIndexResponse createIndexResponse) {}

                        @Override
                        public void onFailure(Exception e) {
                            log.error("create index={} fail in Elasticsearch", indexName);
                        }
                    });
                }
            } catch (Exception e) {
                log.error("Elasticsearch init document error : ", e);
            }
        }
        return bean;
    }

    private Map<String, Object> createMapping(java.lang.reflect.Field[] jdkFields, String indexName) {
        Map<String, Object> mapping = new HashMap<>(16);
        Map<String, Object> properties = new HashMap<>(16);
        // 解析字段
        for (java.lang.reflect.Field jdkField : jdkFields) {
            Map<String, Object> fieldMap = new HashMap<>(4);
            Field[] fields = jdkField.getAnnotationsByType(Field.class);
            if (null == fields) {
                log.warn("current es index ({}), the field ({}) has no @Field Tag", indexName, FastJsonHelper.objectConvertString(jdkField));
                continue;
            }

            boolean hasField = false;
            // 判断是否含有fields字段
            Map<String, Object> fieldsMap = new HashMap<>(4);
            for (int i = 0; i < fields.length; i++) {
                Field index = fields[i];
                if (!index.fields()) {
                    if (hasField) {
                        throw new ElasticsearchException("multiple [fields] value is true has been found in @Field Tag, index : %s, field:%s", indexName, jdkField);
                    }
                    // 构建mapping
                    createProperties(index, fieldMap);
                    hasField = true;
                    continue;
                }
                String fieldsName = StringUtils.isBlank(index.fieldsName()) ? index.type() + i : index.fieldsName();

                Map<String, Object> fieldsProperties = new HashMap<>(4);
                createProperties(index, fieldsProperties);
                fieldsMap.put(fieldsName, fieldsProperties);
            }
            if (!hasField) {
                throw new ElasticsearchException("current es index [%s], the field [%s] need @Field Tag have only one that [fields] value is true", indexName, jdkField);
            }
            // 判断是否包含fields字段
            if (!CollectionUtils.isEmpty(fieldsMap)) {
                fieldMap.put("fields", fieldsMap);
            }
            properties.put(jdkField.getName(), fieldMap);
        }
        mapping.put("properties", properties);
        return mapping;
    }

    private void createProperties(Field field, Map<String, Object> properties) {
        properties.put("type", field.type());
        if (OmsElasticsearchConstants.FiledType.DATE.equals(field.type())) {
            properties.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis");
            // 格式错误的数字将引发异常并拒绝整个文档
            properties.put("ignore_malformed", false);
            // 默认为null，这意味着该字段被视为丢失
            properties.put("null_value", null);
        }
        if (!field.index()) {
            properties.put("index", field.index());
        }
        if (StringUtils.isNotBlank(field.analyzer())) {
            properties.put("analyzer", field.analyzer());
        }
        if (StringUtils.isNotBlank(field.searchAnalyzer())) {
            properties.put("search_analyzer", field.searchAnalyzer());
        }

    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.restHighLevelClient = beanFactory.getBean("restHighLevelClient", RestHighLevelClient.class);
    }
}
