package org.g2.starter.elasticsearch.config.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

/**
 * @author wenxi.wu@hand-china.com 2020/4/18
 */
@ConfigurationProperties(
        prefix = "g2.elasticsearch"
)
public class ElasticsearchProperties {

    /**
     * es 节点 集群节点以 “，” 分开
     */
    private String clusterNodes = "127.0.0.1:9200";
    /**
     * 默认协议
     */
    private String scheme = "http";
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接配置
     */
    private Pool pool;
    /**
     * 是否开启嗅探器
     */
    private boolean enableSniff = false;
    /**
     * 允许重建索引次数
     */
    private int retry;
    /**
     * 是否进行重建索引
     */
    private boolean reIndex;
    /**
     * es监控
     */
    private Monitor monitor;
    /**
     * bulkProcessor配置
     */
    private BulkProcessor bulkProcessor;

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public boolean isEnableSniff() {
        return enableSniff;
    }

    public void setEnableSniff(boolean enableSniff) {
        this.enableSniff = enableSniff;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public boolean isReIndex() {
        return reIndex;
    }

    public void setReIndex(boolean reIndex) {
        this.reIndex = reIndex;
    }

    public BulkProcessor getBulkProcessor() {
        return bulkProcessor;
    }

    public void setBulkProcessor(BulkProcessor bulkProcessor) {
        this.bulkProcessor = bulkProcessor;
    }

    public static class Monitor {
        /**
         * 指定多长时间监测es连接状况，默认10s执行一次
         */
        private Long renewalIntervalSeconds;

        public Long getRenewalIntervalSeconds() {
            return renewalIntervalSeconds;
        }

        public void setRenewalIntervalSeconds(Long renewalIntervalSeconds) {
            this.renewalIntervalSeconds = renewalIntervalSeconds;
        }
    }

    public static class Pool {
        /**
         * 最大连接数
         */
        private Integer maxConnectionNum;
        /**
         * 最大连接
         */
        private Integer maxConnectionPerRoute;
        /**
         * 连接超时
         */
        private Integer connectionTimeout;
        /**
         * 读取超时时间
         */
        private Integer socketTimeout;
        /**
         * 请求超时
         */
        private Integer connectionRequestTimeout;

        public Integer getMaxConnectionNum() {
            return maxConnectionNum;
        }

        public void setMaxConnectionNum(Integer maxConnectionNum) {
            this.maxConnectionNum = maxConnectionNum;
        }

        public Integer getMaxConnectionPerRoute() {
            return maxConnectionPerRoute;
        }

        public void setMaxConnectionPerRoute(Integer maxConnectionPerRoute) {
            this.maxConnectionPerRoute = maxConnectionPerRoute;
        }

        public Integer getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public Integer getSocketTimeout() {
            return socketTimeout;
        }

        public void setSocketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
        }

        public Integer getConnectionRequestTimeout() {
            return connectionRequestTimeout;
        }

        public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
        }
    }

    public static class BulkProcessor {
        /**
         * 指定bulk数量执行一次刷新操作，默认500条刷新一次
         */
        private Integer bulkAction;
        /**
         * 执行bulk数据大小执行一次刷新操作，默认5MB刷新一次
         */
        private DataSize bulkSize;
        /**
         * 并发请求，默认1，设置为0则不使用并发请求
         */
        private Integer concurrent;
        /**
         * 指定固定时间刷新一次，即使没有达到bulkAction或bulkSize，也会刷新一次
         */
        private Duration flushInterval;
        /**
         * 指定回退操作时间。默认1s
         */
        private Duration backoff;
        /**
         * 指定回退操作重试次数，默认3次
         */
        private Integer backoffRetry;

        public BulkProcessor() {
            this.bulkAction = 500;
            this.bulkSize = DataSize.ofMegabytes(5);
            this.concurrent = 1;
            this.flushInterval = Duration.ofSeconds(10);
            this.backoff = Duration.ofSeconds(1);
            this.backoffRetry = 3;
        }

        public Integer getBulkAction() {
            return bulkAction;
        }

        public void setBulkAction(Integer bulkAction) {
            this.bulkAction = bulkAction;
        }

        public DataSize getBulkSize() {
            return bulkSize;
        }

        public void setBulkSize(DataSize bulkSize) {
            this.bulkSize = bulkSize;
        }

        public Integer getConcurrent() {
            return concurrent;
        }

        public void setConcurrent(Integer concurrent) {
            this.concurrent = concurrent;
        }

        public Duration getFlushInterval() {
            return flushInterval;
        }

        public void setFlushInterval(Duration flushInterval) {
            this.flushInterval = flushInterval;
        }

        public Duration getBackoff() {
            return backoff;
        }

        public void setBackoff(Duration backoff) {
            this.backoff = backoff;
        }

        public Integer getBackoffRetry() {
            return backoffRetry;
        }

        public void setBackoffRetry(Integer backoffRetry) {
            this.backoffRetry = backoffRetry;
        }
    }
}
