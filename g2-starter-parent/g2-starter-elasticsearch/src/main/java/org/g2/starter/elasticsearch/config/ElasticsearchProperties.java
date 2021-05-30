package org.g2.starter.elasticsearch.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
     * es监控
     */
    private Monitor monitor;
    /**
     * 是否开启嗅探器
     */
    private boolean enableSniff = false;

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
}
