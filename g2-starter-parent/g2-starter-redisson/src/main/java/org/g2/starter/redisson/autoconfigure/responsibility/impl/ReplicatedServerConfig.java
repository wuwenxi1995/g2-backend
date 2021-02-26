package org.g2.starter.redisson.autoconfigure.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.handler.InvocationHandler;
import org.g2.starter.redisson.autoconfigure.responsibility.AbstractServerConfig;
import org.g2.starter.redisson.config.LockConfigureProperties;
import org.g2.starter.redisson.infra.enums.ServerPattern;
import org.redisson.config.Config;
import org.redisson.config.ReplicatedServersConfig;

import java.util.Arrays;

/**
 * 云托管配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-23
 */
public class ReplicatedServerConfig extends AbstractServerConfig {

    public ReplicatedServerConfig(Config config, LockConfigureProperties properties) {
        super(config, properties);
    }

    @Override
    public Object invoke(InvocationHandler handler) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.REPLICATED.getPattern().equals(pattern)) {
            return handler.proceed();
        }
        ReplicatedServersConfig replicatedServersConfig = config.useReplicatedServers();
        LockConfigureProperties.ReplicatedConfig replicatedConfig = properties.getReplicatedConfig();
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
