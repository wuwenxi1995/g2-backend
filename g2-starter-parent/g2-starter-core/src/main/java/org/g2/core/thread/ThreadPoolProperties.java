package org.g2.core.thread;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置
 *
 * @author wuwenxi 2021-07-16
 */
@ConfigurationProperties(prefix = "g2.thread.pool")
public class ThreadPoolProperties {

    /**
     * 是否启用
     */
    private boolean enable;
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
    /**
     * @see ThreadRejected
     * 线程拒绝策略 可自定义拒绝策略
     */
    private String threadRejected;

    public ThreadPoolProperties() {
        this.enable = false;
        this.corePoolSize = 5;
        this.maxPoolSize = 20;
        this.keepAliveTime = 20;
        this.queueSize = 999;
        this.prefixName = "g2-thread-pool-";
        this.allowCoreThreadTimeOut = false;
        this.threadRejected = ThreadRejected.AbortPolicy.getValue();
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

    public boolean isAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public String getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(String prefixName) {
        this.prefixName = prefixName;
    }

    public String getThreadRejected() {
        return threadRejected;
    }

    public void setThreadRejected(String threadRejected) {
        this.threadRejected = threadRejected;
    }
}
