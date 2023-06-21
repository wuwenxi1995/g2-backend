package org.g2.starter.core.config;

import org.g2.starter.core.config.properties.AsyncTaskProperties;
import org.g2.starter.core.helper.ApplicationContextHelper;
import org.g2.starter.core.helper.AsyncTaskHelper;
import org.g2.starter.core.helper.TransactionalHelper;
import org.g2.starter.core.user.CustomerType;
import org.g2.starter.core.user.PlatformType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wenxi.wu@hand-china.com 2020-11-05
 */
@EnableConfigurationProperties(value = {AsyncTaskProperties.class})
@ComponentScan(basePackages = "org.g2.core")
public class G2CoreAutoConfiguration {

    @Bean
    public ApplicationContextHelper applicationContextHelper() {
        return new ApplicationContextHelper();
    }

    @Bean
    public AsyncTaskHelper asyncTask() {
        return new AsyncTaskHelper();
    }

    @Bean
    public TransactionalHelper transactionalHelper() {
        return new TransactionalHelper();
    }

    @Bean
    @ConditionalOnProperty(prefix = "g2.core.async", value = "enable", havingValue = "true")
    public AsyncTaskExecutor asyncTaskExecutor(AsyncTaskProperties taskProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 最大线程数
        executor.setMaxPoolSize(30);
        // 核心线程数
        executor.setCorePoolSize(15);
        // 线程阻塞队列，如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(99999);
        // 线程名前缀
        executor.setThreadNamePrefix("async-task-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean
    @Scope(value = "singleton")
    public CustomerType customerType() {
        return new CustomerType();
    }

    @Bean
    @Scope(value = "singleton")
    public PlatformType platformType() {
        return new PlatformType();
    }

}
