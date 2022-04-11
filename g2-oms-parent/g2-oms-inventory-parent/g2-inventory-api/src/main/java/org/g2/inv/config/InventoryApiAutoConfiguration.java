package org.g2.inv.config;

import org.g2.inv.infra.feign.InventoryFeignClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2022-04-09
 */
@Configuration
@EnableFeignClients(basePackages = "org.g2.inv.infra.feign")
public class InventoryApiAutoConfiguration {
}
