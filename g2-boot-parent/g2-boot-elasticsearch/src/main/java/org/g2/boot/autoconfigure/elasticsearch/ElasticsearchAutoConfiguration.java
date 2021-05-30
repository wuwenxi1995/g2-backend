package org.g2.boot.autoconfigure.elasticsearch;

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

}
