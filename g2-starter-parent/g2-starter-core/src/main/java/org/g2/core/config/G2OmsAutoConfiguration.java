package org.g2.core.config;

import org.g2.core.helper.ApplicationContextHelper;
import org.g2.core.helper.AsyncTaskHelper;
import org.g2.core.helper.TransactionalHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author wenxi.wu@hand-china.com 2020-11-05
 */
@Configuration
public class G2OmsAutoConfiguration {

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
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(20);
        executor.setCorePoolSize(10);
        executor.setKeepAliveSeconds(30);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-task");
        return executor;
    }
}
