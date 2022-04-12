package org.g2.inv.core.config;

import org.g2.inv.core.config.properties.InvThreadProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wuwenxi 2022-04-09
 */
@Configuration
@EnableConfigurationProperties(value = {InvThreadProperties.class})
@MapperScan(basePackages = "org.g2.inv.core.infra.mapper")
@ComponentScan(basePackages = {"org.g2.inv.core.domain",
        "org.g2.inv.core.infra"})
public class InventoryCoreAutoConfiguration {

    @Bean("invThreadPool")
    @ConditionalOnProperty(prefix = "g2.inv.core.thread", value = "enable", havingValue = "true", matchIfMissing = true)
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(InvThreadProperties properties) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(properties.getCorePoolSize());
        taskExecutor.setMaxPoolSize(properties.getMaxPoolSize());
        taskExecutor.setKeepAliveSeconds(properties.getKeepAliveTime());
        taskExecutor.setThreadNamePrefix(properties.getPrefixName());
        taskExecutor.setQueueCapacity(properties.getQueueSize());
        taskExecutor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());
        // 拒绝继续提交线程
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return taskExecutor;
    }
}
