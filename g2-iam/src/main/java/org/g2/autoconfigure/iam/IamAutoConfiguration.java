package org.g2.autoconfigure.iam;

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
@EnableFeignClients(basePackages = "org.g2.iam")
@ComponentScan(basePackages = {"org.g2.iam.api", "org.g2.iam.app", "org.g2.iam.domain", "org.g2.iam.infra"})
public class IamAutoConfiguration {
}
