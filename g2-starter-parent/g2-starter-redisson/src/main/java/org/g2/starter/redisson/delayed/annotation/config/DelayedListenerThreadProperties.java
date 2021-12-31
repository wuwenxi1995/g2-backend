package org.g2.starter.redisson.delayed.annotation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2021-12-27
 */
@ConfigurationProperties(prefix = "g2.redisson.delayed.thread")
public class DelayedListenerThreadProperties {

    public static final String DELAYED_LISTENER_DEFAULT_THREAD = "delayedListenerExecutor";

    /**
     * 线程池核心线程数
     */
    private int corePoolSize;
    /**
     * 线程池最大线程数
     */
    private int maxPoolSize;
    /**
     * 非核心线程 空闲存活时间
     */
    private int keepAliveTime;
    /**
     * 阻塞队列最大长度
     */
    private int queueSize;
    /**
     * 线程名前缀
     */
    private String prefixName;
    /**
     * 核心线程是否允许在空闲后被销毁
     */
    private boolean allowCoreThreadTimeOut;

    public DelayedListenerThreadProperties() {
        this.corePoolSize = 15;
        this.maxPoolSize = 30;
        this.keepAliveTime = 0;
        this.queueSize = 9999;
        this.prefixName = "delayed-listener-";
        this.allowCoreThreadTimeOut = false;
    }

    public static String getDelayedListenerDefaultThread() {
        return DELAYED_LISTENER_DEFAULT_THREAD;
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

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public String getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(String prefixName) {
        this.prefixName = prefixName;
    }

    public boolean isAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }
}
