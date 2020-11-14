package org.g2.starter.redis.config.properties;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
public class RedisProperties {
    /**
     * 连接池
     */
    private Pool pool;

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
        private int poolMaxIdle;
        /**
         * 最小连接数
         */
        private int poolMinIdle;
        /**
         * 最大连接等待时间
         */
        private long poolMaxWaitTime;
        /**
         * 超时时间
         */
        private long timeoutMillis;


        int getPoolMaxIdle() {
            return poolMaxIdle;
        }

        public void setPoolMaxIdle(int poolMaxIdle) {
            this.poolMaxIdle = poolMaxIdle;
        }

        int getPoolMinIdle() {
            return poolMinIdle;
        }

        public void setPoolMinIdle(int poolMinIdle) {
            this.poolMinIdle = poolMinIdle;
        }

        long getPoolMaxWaitTime() {
            return poolMaxWaitTime;
        }

        public void setPoolMaxWaitTime(long poolMaxWaitTime) {
            this.poolMaxWaitTime = poolMaxWaitTime;
        }

        long getTimeoutMillis() {
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

    /**
     * lettuce连接池配置
     *
     * @return lettuceClientConfiguration
     */
    public LettuceClientConfiguration buildLettuceClientConfiguration() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(pool.getPoolMaxIdle());
        genericObjectPoolConfig.setMinIdle(pool.getPoolMinIdle());
        genericObjectPoolConfig.setMaxTotal(pool.getPoolMaxIdle());
        genericObjectPoolConfig.setMaxWaitMillis(pool.getPoolMaxWaitTime());
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .commandTimeout(Duration.ofMillis(pool.getTimeoutMillis()))
                .build();
    }

}
