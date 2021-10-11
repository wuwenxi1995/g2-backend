package org.g2.starter.zk.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2021-06-18
 */
@ConfigurationProperties(prefix = "g2.lock.zookeeper")
public class ZookeeperLockConfigureProperties {

    private boolean enable;

    private String connectionUrl;

    private int sessionTimeout;

    private int connectionTimeout;

    private Retry retry;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    public static class Retry {
        // 重试时间, ms
        private int baseSleepTimeMs = 1000;
        // 重试次数
        private int maxRetries = 10;

        public int getBaseSleepTimeMs() {
            return baseSleepTimeMs;
        }

        public void setBaseSleepTimeMs(int baseSleepTimeMs) {
            this.baseSleepTimeMs = baseSleepTimeMs;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }
    }
}
