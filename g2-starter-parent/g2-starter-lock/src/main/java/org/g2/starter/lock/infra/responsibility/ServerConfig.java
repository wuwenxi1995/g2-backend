package org.g2.starter.lock.infra.responsibility;

import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.redisson.config.BaseMasterSlaveServersConfig;

import java.net.URISyntaxException;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-23
 */
public interface ServerConfig {

    /**
     * 构建集群配置
     *
     * @param config     集群配置
     * @param baseConfig 自定义配置信息
     */
    void build(BaseMasterSlaveServersConfig config, RedissonConfigureProperties.BaseConfig baseConfig);

    /**
     * 构建ssl配置
     *
     * @param config 集群配置
     * @throws URISyntaxException 异常信息
     */
    void setLockSslConfig(BaseMasterSlaveServersConfig config) throws URISyntaxException;
}
