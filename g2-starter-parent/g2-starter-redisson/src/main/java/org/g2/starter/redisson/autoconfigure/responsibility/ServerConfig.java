package org.g2.starter.redisson.autoconfigure.responsibility;

import org.g2.core.handler.MethodInvocationHandler;

import java.net.URISyntaxException;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-23
 */
public interface ServerConfig<T, E> extends MethodInvocationHandler {

    /**
     * 构建集群配置
     *
     * @param config     集群配置
     * @param baseConfig 自定义配置信息
     */
    default void build(T config, E baseConfig) {
    }

    /**
     * 构建ssl配置
     *
     * @param config 集群配置
     * @throws URISyntaxException 异常信息
     */
    default void setLockSslConfig(T config) throws URISyntaxException {
    }
}
