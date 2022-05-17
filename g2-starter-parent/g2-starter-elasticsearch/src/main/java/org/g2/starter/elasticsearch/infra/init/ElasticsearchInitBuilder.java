package org.g2.starter.elasticsearch.infra.init;

import org.g2.core.helper.ApplicationContextHelper;
import org.g2.starter.elasticsearch.app.serivce.ElasticsearchService;
import org.g2.starter.elasticsearch.infra.annotation.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wuwenxi 2022-04-25
 */
@Component
public class ElasticsearchInitBuilder implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchInitBuilder.class);

    private final ElasticsearchService elasticsearchService;

    public ElasticsearchInitBuilder(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("es init start ...");
        long start = System.currentTimeMillis();
        Map<String, Object> beans = ApplicationContextHelper.getApplicationContext().getBeansWithAnnotation(Document.class);
        beans.values().forEach(bean -> elasticsearchService.createIndex(bean, true));
        log.info("es init end , time: {}", System.currentTimeMillis() - start);
    }
}
