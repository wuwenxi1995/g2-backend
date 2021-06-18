package org.g2.oms.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wenxi.wu@hand-chian.com 2021-01-20
 */
@Configuration
public class OrderEnableConfiguration {

    @Bean
    public ThreadPoolTaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(10);
        //设置最大线程数
        executor.setMaxPoolSize(20);
        //除核心线程外的线程存活时间
        executor.setKeepAliveSeconds(10);
        //如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(10000);
        //线程名称前缀
        executor.setThreadNamePrefix("task-order-");
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
