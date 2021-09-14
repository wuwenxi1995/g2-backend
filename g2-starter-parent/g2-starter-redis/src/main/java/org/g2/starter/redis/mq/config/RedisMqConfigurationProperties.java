package org.g2.starter.redis.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2021-08-23
 */
@ConfigurationProperties(prefix = "g2.redis.mq")
public class RedisMqConfigurationProperties {

    private boolean enable;
    /**
     * redis监听线程池-核心线程数
     */
    private int corePoolSize;
    /**
     * redis监听线程池-最大线程数
     */
    private int maxPoolSize;
    /**
     * redis监听线程池-线程池等待队列大小
     */
    private int queueCapacity;
    /**
     * redis监听线程池-线程存活时间
     */
    private int keepAlive;
    /**
     * redis监听线程池-核心线程是否允许销毁
     */
    private boolean allowCoreThreadTimeOut;

    public RedisMqConfigurationProperties() {
        this.enable = false;
        this.corePoolSize = 24;
        this.maxPoolSize = 48;
        this.queueCapacity = 9999;
        this.keepAlive = 0;
        this.allowCoreThreadTimeOut = false;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }
}
