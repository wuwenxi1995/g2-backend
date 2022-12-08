package org.g2.message.listener.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author wuwenxi 2022-12-06
 */
@ConfigurationProperties(prefix = "o2.redis.message.listener")
public class RedisMessageListenerProperties {

    private boolean enable;

    private Duration delayed;

    private boolean isAutoCommit;

    private Duration autoRollbackTime;

    private int retry;

    private ThreadProperties thread;

    public RedisMessageListenerProperties() {
        this.enable = false;
        this.delayed = Duration.ofSeconds(1);
        this.isAutoCommit = true;
        this.autoRollbackTime = Duration.ofMinutes(5);
        this.retry = 5;
        this.thread = new ThreadProperties();
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Duration getDelayed() {
        return delayed;
    }

    public void setDelayed(Duration delayed) {
        this.delayed = delayed;
    }

    public boolean isAutoCommit() {
        return isAutoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        isAutoCommit = autoCommit;
    }

    public Duration getAutoRollbackTime() {
        return autoRollbackTime;
    }

    public void setAutoRollbackTime(Duration autoRollbackTime) {
        this.autoRollbackTime = autoRollbackTime;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public ThreadProperties getThread() {
        return thread;
    }

    public void setThread(ThreadProperties thread) {
        this.thread = thread;
    }

    public static class ThreadProperties {
        private int coreSize;
        private int maxSize;
        private int keepAliveSeconds;
        private int queueCapacity;
        private boolean allowCoreThreadTimeOut;

        public ThreadProperties() {
            this.coreSize = Runtime.getRuntime().availableProcessors();
            this.maxSize = Runtime.getRuntime().availableProcessors() << 1;
            this.keepAliveSeconds = 10;
            this.queueCapacity = 999;
            this.allowCoreThreadTimeOut = true;
        }

        public int getCoreSize() {
            return coreSize;
        }

        public void setCoreSize(int coreSize) {
            this.coreSize = coreSize;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getKeepAliveSeconds() {
            return keepAliveSeconds;
        }

        public void setKeepAliveSeconds(int keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public boolean isAllowCoreThreadTimeOut() {
            return allowCoreThreadTimeOut;
        }

        public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        }
    }
}
