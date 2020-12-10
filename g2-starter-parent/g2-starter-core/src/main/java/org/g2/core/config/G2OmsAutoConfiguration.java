package org.g2.core.config;

import org.g2.core.helper.ApplicationContextHelper;
import org.g2.core.helper.AsyncTaskHelper;
import org.g2.core.helper.TransactionalHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

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
        // 最大线程数
        executor.setMaxPoolSize(30);
        // 核心线程数
        executor.setCorePoolSize(15);
        // 除核心线程外的线程存活时间
        executor.setKeepAliveSeconds(300);
        // 线程阻塞队列，如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(99999);
        // 线程名前缀
        executor.setThreadNamePrefix("async-task");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
