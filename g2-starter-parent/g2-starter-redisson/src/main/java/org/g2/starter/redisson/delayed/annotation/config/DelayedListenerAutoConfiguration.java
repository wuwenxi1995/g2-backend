package org.g2.starter.redisson.delayed.annotation.config;

import org.g2.starter.redisson.delayed.annotation.DelayedListenerAnnotationPostProcessor;
import org.g2.starter.redisson.delayed.annotation.producer.DelayedMessageProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wuwenxi 2021-12-27
 */
@Configuration
@EnableConfigurationProperties(value = {DelayedListenerThreadProperties.class})
public class DelayedListenerAutoConfiguration {

    @Bean
    @ConditionalOnBean(value = {DelayedListenerAnnotationPostProcessor.class})
    public ThreadPoolTaskExecutor delayedListenerExecutor(DelayedListenerThreadProperties properties) {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置最大线程数
        poolTaskExecutor.setMaxPoolSize(properties.getMaxPoolSize());
        // 设置非核心线程数空闲后存活时间
        poolTaskExecutor.setKeepAliveSeconds(properties.getKeepAliveTime());
        // 设置核心线程数
        poolTaskExecutor.setCorePoolSize(properties.getCorePoolSize());
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

    @Bean
    @ConditionalOnBean(value = {DelayedListenerAnnotationPostProcessor.class})
    public DelayedMessageProducer delayedMessageProducer() {
        return new DelayedMessageProducer();
    }
}
