package org.g2.boot.elasticsearch.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.g2.boot.elasticsearch.app.service.ElasticsearchService;
import org.g2.core.helper.ApplicationContextHelper;
import org.g2.core.util.StringUtil;
import org.g2.starter.elasticsearch.config.spring.builder.RestClientBuildFactory;
import org.g2.starter.elasticsearch.infra.annotation.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

    private static final String SCRIPT = "def id = ctx._source.id;ctx._source.id = id;";
    private static final Map<String, Object> PARAM = new HashMap<>(16);

    private final RestClientBuildFactory restClientBuildFactory;

    public ElasticsearchServiceImpl(RestClientBuildFactory restClientBuildFactory) {
        this.restClientBuildFactory = restClientBuildFactory;
    }

    @Override
    public void reIndex(String indexName) {
        List<String> indexNames = new ArrayList<>();
        if (StringUtils.isBlank(indexName)) {
            indexNames.add(indexName);
        } else {
            Map<String, Object> beans = ApplicationContextHelper.getApplicationContext().getBeansWithAnnotation(Document.class);
            for (Object bean : beans.values()) {
                String beanIndexName = bean.getClass().getAnnotation(Document.class).indexName();
                indexNames.add(beanIndexName);
            }
        }
        for (String name : indexNames) {
            UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
            updateByQueryRequest.indices(name);
            Script script = new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG, SCRIPT, PARAM);
            updateByQueryRequest.setScript(script);
            restClientBuildFactory.restHighLevelClient().updateByQueryAsync(updateByQueryRequest, RequestOptions.DEFAULT, new ActionListener<BulkByScrollResponse>() {
                @Override
                public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
                    log.info("reIndex response : {}", bulkByScrollResponse.getStatus());
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("reIndex error : {}", StringUtil.exceptionString(e));
                }
            });
        }
    }
}
