package org.g2.autoconfigure.scheduler.config;

import org.mybatis.spring.annotation.MapperScan;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

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

    private DataSource dataSource;

    public EnableSchedulerAutoConfiguration(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(30);
        executor.setCorePoolSize(10);
        executor.setQueueCapacity(99999);
        executor.setThreadNamePrefix("scheduler-thread");
        return executor;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        Properties pro = propertiesFactoryBean.getObject();
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);
        factory.setQuartzProperties(Objects.requireNonNull(pro));
        factory.setDataSource(this.dataSource);
        return factory;

    }

    @Bean
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }
}
