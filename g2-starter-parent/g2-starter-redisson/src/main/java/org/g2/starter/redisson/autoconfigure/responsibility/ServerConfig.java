package org.g2.starter.redisson.autoconfigure.responsibility;

import org.g2.core.handler.MethodInvocationHandler;
import org.g2.starter.redisson.config.LockConfigureProperties;
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
    void build(BaseMasterSlaveServersConfig config, LockConfigureProperties.BaseConfig baseConfig);

    /**
     * 构建ssl配置
     *
     * @param config 集群配置
     * @throws URISyntaxException 异常信息
     */
    void setLockSslConfig(BaseMasterSlaveServersConfig config) throws URISyntaxException;
}
