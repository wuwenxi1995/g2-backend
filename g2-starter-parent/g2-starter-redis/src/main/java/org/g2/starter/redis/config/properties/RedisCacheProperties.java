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
}
