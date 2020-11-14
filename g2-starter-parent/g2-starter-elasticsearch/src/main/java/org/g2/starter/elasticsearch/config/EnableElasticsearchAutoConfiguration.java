package org.g2.starter.elasticsearch.config;

import org.g2.starter.elasticsearch.domain.InitRestClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.g2.starter.elasticsearch.infra.init.DocumentInitProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * @author wenxi.wu@hand-china.com 2020/4/18
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestClient.class)
@EnableConfigurationProperties(ElasticsearchProperties.class)
@Import({DocumentInitProcessor.class})
public class EnableElasticsearchAutoConfiguration {

    private final ElasticsearchProperties elasticsearchProperties;

    public EnableElasticsearchAutoConfiguration(ElasticsearchProperties elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public InitRestClientBuilder initRestClientBuilder() {
        return new InitRestClientBuilder(elasticsearchProperties);
    }

    @Bean
    @Order(-1)
    @ConditionalOnBean(InitRestClientBuilder.class)
    public RestHighLevelClient restHighLevelClient(InitRestClientBuilder initRestClientBuilder) {
        return initRestClientBuilder.getRestHighLevelClient();
    }

    @Bean
    public RestClient restClient(InitRestClientBuilder initRestClientBuilder) {
        return initRestClientBuilder.getRestClient();
    }
}
