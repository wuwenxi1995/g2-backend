package org.g2.boot.message.config;

import org.g2.boot.message.config.properties.MessageClientProperties;
import org.g2.boot.message.service.async.MessageAsyncService;
import org.g2.boot.message.service.client.MessageClient;
import org.g2.boot.message.feign.MessageRemoteService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-05
 */
@Configuration
@EnableAsync
@EnableFeignClients(basePackageClasses = MessageRemoteService.class)
@EnableConfigurationProperties(value = {MessageClientProperties.class})
public class MessageClientConfiguration {

    @Bean
    public MessageClient messageClient() {
        return new MessageClient();
    }

    @Bean
    public MessageAsyncService messageAsyncService() {
        return new MessageAsyncService();
    }
}
