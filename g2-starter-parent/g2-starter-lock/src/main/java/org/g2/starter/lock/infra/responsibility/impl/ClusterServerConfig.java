package org.g2.starter.lock.infra.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.handler.InvocationHandler;
import org.g2.starter.lock.autoconfigure.RedissonBuildFactory;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.infra.enums.ServerPattern;
import org.g2.starter.lock.infra.responsibility.AbstractServerConfig;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * redis集群配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Component
public class ClusterServerConfig extends AbstractServerConfig {

    public ClusterServerConfig(RedissonBuildFactory redissonBuildFactory, RedissonConfigureProperties properties) {
        super(redissonBuildFactory.getConfig(), properties);
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

    @Override
    public int getOrder() {
        return 3;
    }
}
