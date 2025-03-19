package org.g2.starter.core.thread;

import org.g2.starter.core.thread.properties.ScheduledPoolProperties;
import org.g2.starter.core.thread.properties.ThreadPoolProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wuwenxi 2021-07-16
 */
@Configuration
@EnableConfigurationProperties({ThreadPoolProperties.class, ScheduledPoolProperties.class})
public class TheadPoolAutoConfiguration {

    @ConditionalOnProperty(prefix = "g2.thread.pool", name = "enable", havingValue = "true")
    @Bean(name = "g2ThreadPool")
    @Order
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(ThreadPoolProperties properties) throws Exception {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        poolTaskExecutor.setCorePoolSize(properties.getCorePoolSize());
        // 设置最大线程数
        poolTaskExecutor.setMaxPoolSize(properties.getMaxPoolSize());
        // 设置非核心线程数空闲后存活时间
        poolTaskExecutor.setKeepAliveSeconds(properties.getKeepAliveTime());
        // 阻塞队列长度
        // 如果不设置，将创建无界的阻塞队列LinkedBlockingQueue可能造成oom
        // 如果小于0，将创建没有容量的阻塞队列SynchronousQueue
        poolTaskExecutor.setQueueCapacity(properties.getQueueSize());
        // 设置是否允许核心线程空闲后被销毁
        poolTaskExecutor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());
        // 设置线程名前缀
        poolTaskExecutor.setThreadNamePrefix(properties.getPrefixName());
        // 设置拒绝策略
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return poolTaskExecutor;
    }

    @ConditionalOnProperty(prefix = "g2.thread.scheduled", name = "enable", havingValue = "true")
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(ScheduledPoolProperties properties) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(properties.getCorePoolSize());
        scheduler.setDaemon(properties.isDaemon());
        scheduler.setThreadNamePrefix(properties.getPrefixName());
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return scheduler;
    }
}
