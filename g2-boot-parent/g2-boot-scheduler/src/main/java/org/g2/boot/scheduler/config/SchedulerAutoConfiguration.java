package org.g2.boot.scheduler.config;

import org.g2.boot.scheduler.infra.feign.SchedulerFeignClient;
import org.g2.boot.scheduler.infra.init.JobExecuteInit;
import org.g2.boot.scheduler.infra.init.RefreshExecutor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-china.com 2020-11-02
 */
@ComponentScan(basePackages = "org.g2.boot.scheduler")
@EnableFeignClients(basePackageClasses = {SchedulerFeignClient.class})
@Configuration
@EnableConfigurationProperties(SchedulerConfig.class)
public class SchedulerAutoConfiguration {

    @Bean
    public JobExecuteInit jobExecuteInit() {
        return new JobExecuteInit();
    }

    @Bean
    public RefreshExecutor refreshExecutor() {
        return new RefreshExecutor();
    }
}
