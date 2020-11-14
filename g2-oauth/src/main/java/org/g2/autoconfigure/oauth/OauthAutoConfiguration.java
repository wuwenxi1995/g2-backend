package org.g2.autoconfigure.oauth;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wenxi.wu@hand-china.com 2020-11-13
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties
@EnableFeignClients(basePackages = "org.g2.oauth")
@ComponentScan(basePackages = {"org.g2.oauth.api", "org.g2.oauth.app", "org.g2.oauth.domain", "org.g2.oauth.infra"})
public class OauthAutoConfiguration {
}
