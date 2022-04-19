package org.g2.inv.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2022-04-11
 */
@ConfigurationProperties(prefix = "g2.inv.core.thread")
public class InvThreadProperties {
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

    public InvThreadProperties() {
        this.enable = true;
        this.corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        this.maxPoolSize = this.corePoolSize * 2;
        this.keepAliveTime = 10;
        this.queueSize = 1000;
        this.prefixName = "inv-thread-pool-";
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
