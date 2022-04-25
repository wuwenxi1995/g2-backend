package org.g2.starter.elasticsearch.infra.init;

import org.g2.core.helper.ApplicationContextHelper;
import org.g2.core.task.TaskHandler;
import org.g2.starter.elasticsearch.app.serivce.ElasticsearchIndexService;
import org.g2.starter.elasticsearch.infra.annotation.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wuwenxi 2022-04-25
 */
@Component
public class ElasticsearchInitBuilder extends TaskHandler {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchInitBuilder.class);

    private final ElasticsearchIndexService elasticsearchIndexService;

    public ElasticsearchInitBuilder(ElasticsearchIndexService elasticsearchIndexService) {
        this.elasticsearchIndexService = elasticsearchIndexService;
    }

    @Override
    protected void run() {
        log.info("es init start ...");
        long start = System.currentTimeMillis();
        Map<String, Object> beans = ApplicationContextHelper.getApplicationContext().getBeansWithAnnotation(Document.class);
        beans.values().forEach(bean -> elasticsearchIndexService.createIndex(bean, true));
        log.info("es init end , time: {}", System.currentTimeMillis() - start);
    }
}
