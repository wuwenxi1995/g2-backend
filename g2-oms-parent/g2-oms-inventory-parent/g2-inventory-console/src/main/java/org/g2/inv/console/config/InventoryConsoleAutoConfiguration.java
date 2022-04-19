package org.g2.inv.console.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2022-04-09
 */
@Configuration
@ComponentScan(basePackages = {
        "org.g2.inv.console.api.controller",
        "org.g2.inv.console.app.service"})
public class InventoryConsoleAutoConfiguration {
}
