package org.g2.starter.elasticsearch.config;

import org.g2.starter.elasticsearch.config.properties.ElasticsearchProperties;
import org.g2.starter.elasticsearch.infra.factory.RestClientBuildFactory;
import org.g2.starter.elasticsearch.infra.monitor.RestClientUnavailableMonitor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-china.com 2020/4/18
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
@ComponentScan(basePackages = {"org.g2.starter.elasticsearch.api",
        "org.g2.starter.elasticsearch.app",
        "org.g2.starter.elasticsearch.infra"})
public class EnableElasticsearchAutoConfiguration {

    @Bean
    public RestClientUnavailableMonitor restClientUnavailableMonitor(RestClientBuildFactory restClientBuildFactory) {
        return new RestClientUnavailableMonitor(restClientBuildFactory);
    }
}
