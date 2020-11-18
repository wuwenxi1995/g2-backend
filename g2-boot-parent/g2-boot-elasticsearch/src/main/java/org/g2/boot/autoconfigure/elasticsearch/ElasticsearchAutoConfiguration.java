package org.g2.boot.autoconfigure.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.g2.boot.elasticsearch.domain.repository.BaseRepository;
import org.g2.boot.elasticsearch.infra.repository.impl.BaseRepositoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-china.com 2020-11-18
 */
@ComponentScan(basePackages = {
        "org.g2.boot.elasticsearch.api",
        "org.g2.boot.elasticsearch.app",
        "org.g2.boot.elasticsearch.domain",
        "org.g2.boot.elasticsearch.infra",
})
@Configuration
public class ElasticsearchAutoConfiguration {

    @Bean
    @ConditionalOnBean(RestHighLevelClient.class)
    public BaseRepository baseRepository(RestHighLevelClient restHighLevelClient) {
        return new BaseRepositoryImpl(restHighLevelClient);
    }
}
