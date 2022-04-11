package org.g2.inv.calculate.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2022-04-11
 */
@Configuration
@ComponentScan(basePackages = {"org.g2.inv.calculate.infra",
        "org.g2.inv.calculate.app"})
public class InvCalculateAutoConfiguration {
}
