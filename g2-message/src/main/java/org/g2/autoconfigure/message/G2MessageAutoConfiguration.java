package org.g2.autoconfigure.message;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
@Configuration
@EnableConfigurationProperties
@EnableAsync
@ComponentScan({"org.g2.message.api","org.g2.message.app","org.g2.message.domain","org.g2.message.infra"})
public class G2MessageAutoConfiguration {
}
