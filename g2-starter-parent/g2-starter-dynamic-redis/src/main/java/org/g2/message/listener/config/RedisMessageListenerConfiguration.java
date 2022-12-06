package org.g2.message.listener.config;

import org.g2.message.listener.config.processor.RedisMessageListenerBeanProcessor;
import org.g2.message.listener.config.properties.RedisMessageListenerProperties;
import org.g2.message.listener.factory.RedisMessageListenerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wuwenxi 2022-12-06
 */
@Configuration
@ConditionalOnProperty(prefix = "g2.redis.message.listener", name = "enable", havingValue = "true")
@EnableConfigurationProperties(RedisMessageListenerProperties.class)
public class RedisMessageListenerConfiguration {

    @Bean
    public RedisMessageListenerBeanProcessor messageListenerBeanProcessor(RedisMessageListenerFactory redisMessageListenerFactory) {
        return new RedisMessageListenerBeanProcessor(redisMessageListenerFactory);
    }

    @Bean
    public RedisMessageListenerFactory redisMessageListenerFactory() {
        return new RedisMessageListenerFactory();
    }

    @Bean
    public ThreadPoolTaskExecutor redisMessageListenerExecutor(RedisMessageListenerProperties properties) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(properties.getThread().getCoreSize());
        taskExecutor.setMaxPoolSize(properties.getThread().getMaxSize());
        taskExecutor.setKeepAliveSeconds(properties.getThread().getKeepAliveSeconds());
        taskExecutor.setQueueCapacity(properties.getThread().getQueueCapacity());
        taskExecutor.setAllowCoreThreadTimeOut(properties.getThread().isAllowCoreThreadTimeOut());
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.setThreadNamePrefix("redisMessageLister-");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskScheduler redisMessageListenerScheduler(RedisMessageListenerProperties properties) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(properties.getThread().getCoreSize());
        taskScheduler.setDaemon(true);
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        return taskScheduler;
    }
}
