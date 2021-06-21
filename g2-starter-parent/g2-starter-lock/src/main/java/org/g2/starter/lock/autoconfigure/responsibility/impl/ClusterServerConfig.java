package org.g2.starter.lock.autoconfigure.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.handler.InvocationHandler;
import org.g2.starter.lock.autoconfigure.responsibility.AbstractServerConfig;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.infra.enums.ServerPattern;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;

import java.util.Arrays;

/**
 * redis集群配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class ClusterServerConfig extends AbstractServerConfig {

    public ClusterServerConfig(Config config, RedissonConfigureProperties properties) {
        super(config, properties);
    }

    @Override
    public Object invoke(InvocationHandler handler) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.CLUSTER.getPattern().equals(pattern)) {
            return handler.proceed();
        }
        ClusterServersConfig clusterServerConfig = config.useClusterServers();
        RedissonConfigureProperties.ClusterConfig clusterConfig = properties.getClusterConfig();
        build(clusterServerConfig, clusterConfig);
        String[] addressArr = clusterConfig.getNodeAddresses().split(BaseConstants.Symbol.COMMA);
        Arrays.asList(addressArr).forEach(address -> clusterServerConfig.addNodeAddress(addressFormat(address)));
        clusterServerConfig.setScanInterval((int) clusterConfig.getScanInterval());
        setLockSslConfig(clusterServerConfig);
        return config;
    }
}
