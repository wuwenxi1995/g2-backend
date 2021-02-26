package org.g2.starter.redis.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.g2.starter.redis.infra.enums.RedisServerPattern;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wenxi.wu@hand-china.com on 2020/4/11 17:53
 */
@ConfigurationProperties(
        prefix = "g2.redis.cache"
)
@Setter
@Getter
public class RedisCacheProperties{

    /**
     * Redis模式
     * @see RedisServerPattern
     */
    private String pattern;
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

    public RedisCacheProperties() {
        this.pattern = RedisServerPattern.STANDALONE.getPattern();
        this.dbIndex = 0;
        this.pool = new Pool();
    }

    @Setter
    @Getter
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

        private Pool() {
        }
    }

    @Setter
    @Getter
    public static class Sentinel {

        /**
         * 哨兵模式主节点 "host:port"
         */
        private String master;

        /**
         * 集群节点，以"host:port"加","进行分割
         */
        private String nodes;
    }

    @Setter
    @Getter
    public static class Cluster {
        /**
         * 集群节点，以"host:port"加","进行分割
         */
        private String nodes;

        /**
         * 最大重定向数
         */
        private Integer maxRedirect;
    }
}
