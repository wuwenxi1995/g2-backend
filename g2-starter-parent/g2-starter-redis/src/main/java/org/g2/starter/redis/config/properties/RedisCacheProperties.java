package org.g2.starter.redis.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wenxi.wu@hand-china.com on 2020/4/11 17:53
 */
@ConfigurationProperties(
        prefix = "g2.redis.cache"
)
public class RedisCacheProperties extends RedisProperties {
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
     * 哨兵模式
     */
    private Sentinel sentinel;
    /**
     * 集群模式
     */
    private Cluster cluster;
    /**
     * 连接池
     */
    private Pool pool;


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

    public Sentinel getSentinel() {
        return sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
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
        /**
         * 超时时间
         */
        private long timeoutMillis = 3000;


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

        public long getTimeoutMillis() {
            return timeoutMillis;
        }

        public void setTimeoutMillis(long timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
        }
    }

    public static class Sentinel {

        /**
         * 哨兵模式主节点 "host:port"
         */
        private String master;

        /**
         * 集群节点，以"host:port"加","进行分割
         */
        private String nodes;

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public String getNodes() {
            return nodes;
        }

        public void setNodes(String nodes) {
            this.nodes = nodes;
        }
    }

    public static class Cluster {
        /**
         * 集群节点，以"host:port"加","进行分割
         */
        private String nodes;

        /**
         * 最大重定向数
         */
        private Integer maxRedirect;

        public String getNodes() {
            return nodes;
        }

        public void setNodes(String nodes) {
            this.nodes = nodes;
        }

        public Integer getMaxRedirect() {
            return maxRedirect;
        }

        public void setMaxRedirect(Integer maxRedirect) {
            this.maxRedirect = maxRedirect;
        }
    }
}
