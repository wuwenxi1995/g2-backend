package org.g2.inv.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2022-04-15
 */
@Configuration
@ComponentScan(basePackages = {"org.g2.inv.api",
        "org.g2.inv.app",
        "org.g2.inv.infra"})
public class EnableInvConfiguration {
}
