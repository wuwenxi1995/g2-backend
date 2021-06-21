package org.g2.starter.lock.infra.enums;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public enum ServerPattern {

    /**
     * 单节点
     */
    SINGLE("single"),
    /**
     * 集群模式
     */
    CLUSTER("cluster"),
    /**
     * 哨兵模式
     */
    SENTINEL("sentinel"),
    /**
     *
     */
    REPLICATED("replicated"),
    /**
     * 主从模式
     */
    MASTER_SLAVE("master_slave");

    private String pattern;

    ServerPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
