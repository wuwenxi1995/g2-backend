package org.g2.boot.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-china.com 2020-11-06
 */
@Component
@ConfigurationProperties(prefix = "g2.scheduler")
public class SchedulerConfig {

    /**
     * 执行器编码
     */
    private String executorCode;

    /**
     * 是否自动注册
     */
    private boolean autoRegistry = true;

    /**
     * 重试时间间隔
     */
    private int retry = 60;

    /**
     * 重试次数
     */
    private int retryTime = 5;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 20;

    /**
     * 核心线程数
     */
    private int corePoolSize = 5;

    private String getExecutorCode() {
        return executorCode;
    }

    private boolean getAutoRegistry() {
        return autoRegistry;
    }

    private int getRetry() {
        return retry;
    }

    private int getRetryTime() {
        return retryTime;
    }

    private int getMaxPoolSize() {
        return maxPoolSize;
    }

    private int getCorePoolSize() {
        return corePoolSize;
    }

    public SchedulerConfig setExecutorCode(String executorCode) {
        this.executorCode = executorCode;
        return this;
    }

    public SchedulerConfig setAutoRegistry(boolean autoRegistry) {
        this.autoRegistry = autoRegistry;
        return this;
    }

    public SchedulerConfig setRetry(int retry) {
        this.retry = retry;
        return this;
    }

    public SchedulerConfig setRetryTime(int retryTime) {
        this.retryTime = retryTime;
        return this;
    }

    public SchedulerConfig setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public SchedulerConfig setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }
}
