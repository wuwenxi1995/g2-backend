package org.g2.inv.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2022-04-09
 */
@Configuration
@EnableFeignClients(basePackages = "org.g2.inv.infra.feign")
@ComponentScan(basePackages = {"org.g2.inv.app", "org.g2.inv.infra"})
public class InventoryApiAutoConfiguration {
}
