package org.g2.starter.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2022-04-18
 */
@ConfigurationProperties(prefix = "g2.core.async")
public class AsyncTaskProperties {

    private boolean enable;
    /**
     * 最大线程数
     */
    private int maxThread;
    /**
     * 核心线程数
     */
    private int coreThread;
    /**
     * 空闲线程存活时间
     */
    private long keepAliveSeconds;
    /**
     * 线程阻塞队列长度
     */
    private long queueCapacity;

    public AsyncTaskProperties() {
        this.enable = false;
        this.coreThread = Runtime.getRuntime().availableProcessors();
        this.maxThread = coreThread * 2;
        this.keepAliveSeconds = 15;
        this.queueCapacity = 9999;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    public int getCoreThread() {
        return coreThread;
    }

    public void setCoreThread(int coreThread) {
        this.coreThread = coreThread;
    }

    public long getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(long keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public long getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(long queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
