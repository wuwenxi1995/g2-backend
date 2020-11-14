package org.g2.autoconfigure.gateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wenxi.wu@hand-china.com 2020-11-13
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties
@ComponentScan(basePackages = {"org.g2.gateway.api", "org.g2.gateway.app", "org.g2.gateway.domain", "org.g2.gateway.infra"})
public class GatewayAutoConfiguration {
}
