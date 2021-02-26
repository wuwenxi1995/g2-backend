package org.g2.starter.redis.infra.enums;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
public enum RedisServerPattern {

    /**
     * 单节点模式
     */
    STANDALONE("standalone"),
    /**
     * 主从模式
     */
    MASTER_SLAVE("master_slave"),
    /**
     * 哨兵模式
     */
    SENTINEL("sentinel"),
    /**
     * 集群模式
     */
    CLUSTER("cluster");
    private String pattern;

    private RedisServerPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
