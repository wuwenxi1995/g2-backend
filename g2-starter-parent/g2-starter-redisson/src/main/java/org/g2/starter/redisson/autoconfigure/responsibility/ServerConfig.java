package org.g2.starter.redisson.autoconfigure.responsibility;

import org.g2.core.handler.InvocationHandler;

import java.net.URISyntaxException;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-23
 */
public interface ServerConfig<T, E> {

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

    /**
     * 构建redisson客户端其他配置信息
     *
     * @param handler handler
     * @return object
     * @throws Exception 异常信息
     */
    Object invoke(InvocationHandler handler) throws Exception;
}
