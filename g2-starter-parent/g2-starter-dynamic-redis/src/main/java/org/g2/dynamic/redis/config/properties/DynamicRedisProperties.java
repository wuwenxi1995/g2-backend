package org.g2.dynamic.redis.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wuwenxi 2021-12-07
 */
@ConfigurationProperties(prefix = "g2.redis")
public class DynamicRedisProperties {

    /**
     * 是否开启动态数据库切换，默认开启
     */
    private boolean dynamicDatabase;
    /**
     * 是否开启动态redis客户端切换，默认开启
     */
    private boolean sharding;

    /**
     * 动态redis客户端数量
     */
    private int shardingNum;

    public DynamicRedisProperties() {
        this.dynamicDatabase = true;
        this.sharding = true;
        this.shardingNum = 5;
    }

    public boolean isDynamicDatabase() {
        return dynamicDatabase;
    }

    public void setDynamicDatabase(boolean dynamicDatabase) {
        this.dynamicDatabase = dynamicDatabase;
    }

    public boolean isSharding() {
        return sharding;
    }

    public void setSharding(boolean sharding) {
        this.sharding = sharding;
    }

    public int getShardingNum() {
        return shardingNum;
    }

    public void setShardingNum(int shardingNum) {
        this.shardingNum = shardingNum;
    }
}
