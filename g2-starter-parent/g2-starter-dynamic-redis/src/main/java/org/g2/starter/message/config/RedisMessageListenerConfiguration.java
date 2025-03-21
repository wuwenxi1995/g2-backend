package org.g2.starter.message.config;

import org.g2.starter.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.starter.message.config.properties.RedisMessageListenerProperties;
import org.g2.starter.message.repository.RedisQueueRepository;
import org.g2.starter.message.repository.impl.RedisQueueRepositoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wuwenxi 2022-12-06
 */
@Configuration
@ConditionalOnProperty(prefix = "g2.redis.message.listener", name = "enable", havingValue = "true")
@EnableConfigurationProperties(RedisMessageListenerProperties.class)
@Import(RedisMessageListenerSelector.class)
public class RedisMessageListenerConfiguration {

    @Bean
    public RedisQueueRepository redisQueueRepository(DynamicRedisHelper dynamicRedisHelper) {
        return new RedisQueueRepositoryImpl(dynamicRedisHelper);
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
}
