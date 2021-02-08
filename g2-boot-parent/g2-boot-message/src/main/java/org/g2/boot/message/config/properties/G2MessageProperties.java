package org.g2.boot.message.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
@ConfigurationProperties(prefix = "g2.message.async")
public class G2MessageProperties {

    /**
     * 最大线程数
     */
    private int maxThread = 30;
    /**
     * 核心线程数
     */
    private int coreThread = 15;
    /**
     * 空闲线程存活时间
     */
    private long keepAliveSeconds = 60;
    /**
     * 线程阻塞队列长度
     */
    private long queueCapacity = 99999;


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
