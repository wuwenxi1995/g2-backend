package org.g2.autoconfigura.scheduler.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wenxi.wu@hand-china.com 2020-11-02
 */
@ComponentScan(basePackages = {"org.g2.scheduler.api",
        "org.g2.scheduler.app",
        "org.g2.scheduler.infra",
        "org.g2.scheduler.domain",
        "org.g2.scheduler.config"})
@Configuration
@EnableAsync
@EnableScheduling
@MapperScan(basePackages = {"org.g2.scheduler.infra.mapper"})
public class EnableSchedulerAutoConfiguration {
}
