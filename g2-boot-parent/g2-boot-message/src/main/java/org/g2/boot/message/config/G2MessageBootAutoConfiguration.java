package org.g2.boot.message.config;

import org.g2.boot.message.config.properties.G2MessageProperties;
import org.g2.boot.message.service.async.MessageAsyncService;
import org.g2.boot.message.service.client.MessageClient;
import org.g2.boot.message.feign.MessageRemoteService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-05
 */
@Configuration
@EnableAsync
@EnableFeignClients(basePackageClasses = MessageRemoteService.class)
@EnableConfigurationProperties(value = {G2MessageProperties.class})
public class G2MessageBootAutoConfiguration {

    @Bean
    public MessageClient messageClient() {
        return new MessageClient();
    }

    @Bean
    public MessageAsyncService messageAsyncService() {
        return new MessageAsyncService();
    }

    @Bean
    public AsyncTaskExecutor messageAsyncTask(G2MessageProperties messageProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 最大线程数
        executor.setMaxPoolSize(messageProperties.getMaxThread());
        // 核心线程数
        executor.setCorePoolSize(messageProperties.getCoreThread());
        // 线程阻塞队列，如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity((int) messageProperties.getQueueCapacity());
        // 线程名前缀
        executor.setThreadNamePrefix("message-task");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
