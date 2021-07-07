package org.g2.starter.lock.infra.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.chain.invoker.ChainInvoker;
import org.g2.starter.lock.autoconfigure.RedissonBuildFactory;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.infra.enums.ServerPattern;
import org.g2.starter.lock.infra.responsibility.AbstractServerConfig;
import org.redisson.config.ReplicatedServersConfig;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 云托管配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-23
 */
@Component
public class ReplicatedServerConfig extends AbstractServerConfig {

    public ReplicatedServerConfig(RedissonBuildFactory redissonBuildFactory, RedissonConfigureProperties properties) {
        super(redissonBuildFactory.getConfig(), properties);
    }

    @Override
    public Object invoke(ChainInvoker invoker) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.REPLICATED.getPattern().equals(pattern)) {
            return invoker.proceed();
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

    @Override
    public int getOrder() {
        return 4;
    }
}
