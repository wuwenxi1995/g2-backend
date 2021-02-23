package org.g2.starter.redisson.autoconfigure.responsibility;

import org.g2.starter.redisson.autoconfigure.RedissonAutoConfiguration;
import org.g2.starter.redisson.config.LockConfigureProperties;
import org.redisson.config.BaseMasterSlaveServersConfig;
import org.redisson.config.Config;

import java.net.URISyntaxException;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-23
 */
public interface ServerConfig<T extends BaseMasterSlaveServersConfig, E extends LockConfigureProperties.BaseConfig> {

    /**
     * 构建集群配置
     *
     * @param config     集群配置
     * @param baseConfig 自定义配置信息
     */
    void build(T config, E baseConfig);

    /**
     * 构建ssl配置
     *
     * @param config     集群配置
     * @throws URISyntaxException 异常信息
     */
    void setLockSslConfig(T config) throws URISyntaxException;

    /**
     * 构建redisson客户端其他配置信息
     *
     * @param handler handler
     * @return object
     * @throws URISyntaxException 异常信息
     */
    Object invoke(RedissonAutoConfiguration.RedissonClientAutoConfigureHandler handler) throws URISyntaxException;
}
