package org.g2.inv.console.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wuwenxi 2022-04-09
 */
@Configuration
@ComponentScan(basePackages = {"org.g2.inv"})
@EnableFeignClients(basePackages = {"org.g2.inv"})
@EnableKafka
@EnableAspectJAutoProxy
@EnableAsync
@EnableConfigurationProperties
public class InvConsoleAutoConfiguration {
}
