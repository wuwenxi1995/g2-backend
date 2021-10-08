package org.g2.starter.delayed.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wuwenxi 2021-10-08
 */
@Component
@ConfigurationProperties(prefix = "g2.redis.delayed")
public class DelayedQueueProperties {
    private boolean isEnable;
    private Executor executor;

    public DelayedQueueProperties() {
        this.isEnable = true;
        this.executor = new Executor();
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public static class Executor {
        private boolean isEnable;
        private int coreSize;
        private int maxSize;
        private int keepAliveSecond;
        private int queueCapacity;
        private boolean allowCoreThreadTimeOut;

        public Executor() {
            this.isEnable = false;
            this.coreSize = 1;
            this.maxSize = Integer.MAX_VALUE;
            this.keepAliveSecond = 60;
            this.queueCapacity = Integer.MAX_VALUE;
            this.allowCoreThreadTimeOut = false;
        }

        public boolean isEnable() {
            return isEnable;
        }

        public void setEnable(boolean enable) {
            isEnable = enable;
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

        public int getKeepAliveSecond() {
            return keepAliveSecond;
        }

        public void setKeepAliveSecond(int keepAliveSecond) {
            this.keepAliveSecond = keepAliveSecond;
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
