package org.g2.starter.lock.autoconfigure.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.handler.InvocationHandler;
import org.g2.starter.lock.autoconfigure.responsibility.AbstractServerConfig;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.infra.enums.ServerPattern;
import org.redisson.config.Config;
import org.redisson.config.ReplicatedServersConfig;

import java.util.Arrays;

/**
 * 云托管配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-23
 */
public class ReplicatedServerConfig extends AbstractServerConfig {

    public ReplicatedServerConfig(Config config, RedissonConfigureProperties properties) {
        super(config, properties);
    }

    @Override
    public Object invoke(InvocationHandler handler) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.REPLICATED.getPattern().equals(pattern)) {
            return handler.proceed();
        }
        ReplicatedServersConfig replicatedServersConfig = config.useReplicatedServers();
        RedissonConfigureProperties.ReplicatedConfig replicatedConfig = properties.getReplicatedConfig();
        //
        build(replicatedServersConfig, replicatedConfig);
        //
        String[] addressArr = replicatedConfig.getNodeAddresses().split(BaseConstants.Symbol.COMMA);
        Arrays.asList(addressArr).forEach(address -> replicatedServersConfig.addNodeAddress(addressFormat(address)));
        replicatedServersConfig.setDatabase(replicatedConfig.getDatabase())
                .setScanInterval(replicatedConfig.getScanInterval())
                .setDnsMonitoringInterval(replicatedConfig.getDnsMonitoringInterval());
        setLockSslConfig(replicatedServersConfig);
        return config;
    }
}
