package org.g2.starter.redisson.autoconfigure.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.starter.redisson.autoconfigure.RedissonAutoConfiguration;
import org.g2.starter.redisson.autoconfigure.responsibility.AbstractServerConfig;
import org.g2.starter.redisson.config.LockConfigureProperties;
import org.g2.starter.redisson.infra.enums.ServerPattern;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;

import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * redis集群配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class ClusterServerConfig extends AbstractServerConfig {

    public ClusterServerConfig(Config config, LockConfigureProperties properties) {
        super(config, properties);
    }

    @Override
    public Object invoke(RedissonAutoConfiguration.RedissonClientAutoConfigureHandler handler) throws URISyntaxException {
        String pattern = properties.getPattern();
        if (!ServerPattern.CLUSTER.getPattern().equals(pattern)) {
            return handler.proceed();
        }
        ClusterServersConfig clusterServerConfig = config.useClusterServers();
        LockConfigureProperties.ClusterConfig clusterConfig = properties.getClusterConfig();
        build(clusterServerConfig, clusterConfig);
        String[] addressArr = clusterConfig.getNodeAddresses().split(BaseConstants.Symbol.COMMA);
        Arrays.asList(addressArr).forEach(address -> clusterServerConfig.addNodeAddress(addressFormat(address)));
        clusterServerConfig.setScanInterval((int) clusterConfig.getScanInterval());
        clusterServerConfig.setClientName(properties.getClientName());
        return config;
    }
}
