package org.g2.starter.redis.config.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@ConfigurationProperties(
        prefix = "g2.redis.shard"
)
public class RedisShardingProperties {

    /**
     * 是否开启多实例
     */
    private boolean enable = false;

    /**
     * 连接池
     */
    private Pool pool;

    /**
     * 多实例配置
     */
    private Map<String, RedisShardingConnection> instances;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<String, RedisShardingConnection> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, RedisShardingConnection> instances) {
        this.instances = instances;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public static class RedisShardingConnection {

        /**
         * 主机地址
         */
        private String host;
        /**
         * 端口
         */
        private int port;
        /**
         * 连接密码
         */
        private String password;
        /**
         * 库
         */
        private int dbIndex;

        /**
         * 连接超时时间
         */
        private long timeoutMillis;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getDbIndex() {
            return dbIndex;
        }

        public void setDbIndex(int dbIndex) {
            this.dbIndex = dbIndex;
        }

        public long getTimeoutMillis() {
            return timeoutMillis;
        }

        public void setTimeoutMillis(long timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
        }
    }

    public static class Pool {
        /**
         * 最大连接数
         */
        private int poolMaxIdle = 8;
        /**
         * 最小连接数
         */
        private int poolMinIdle = 4;
        /**
         * 最大连接等待时间
         */
        private long poolMaxWaitTime = 30000;


        public int getPoolMaxIdle() {
            return poolMaxIdle;
        }

        public void setPoolMaxIdle(int poolMaxIdle) {
            this.poolMaxIdle = poolMaxIdle;
        }

        public int getPoolMinIdle() {
            return poolMinIdle;
        }

        public void setPoolMinIdle(int poolMinIdle) {
            this.poolMinIdle = poolMinIdle;
        }

        public long getPoolMaxWaitTime() {
            return poolMaxWaitTime;
        }

        public void setPoolMaxWaitTime(long poolMaxWaitTime) {
            this.poolMaxWaitTime = poolMaxWaitTime;
        }
    }
}
