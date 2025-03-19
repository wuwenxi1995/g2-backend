package org.g2.starter.redisson.config.responsibility.impl;

import org.g2.starter.core.base.BaseConstants;
import org.g2.starter.core.chain.invoker.ChainInvoker;
import org.g2.starter.redisson.config.RedissonBuildFactory;
import org.g2.starter.redisson.config.responsibility.AbstractServerConfig;
import org.g2.starter.redisson.lock.config.RedissonConfigureProperties;
import org.g2.starter.redisson.lock.infra.enums.ServerPattern;
import org.redisson.config.MasterSlaveServersConfig;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * redis主从配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Component
public class MasterSlaveServerConfig extends AbstractServerConfig {

    public MasterSlaveServerConfig(RedissonBuildFactory redissonBuildFactory, RedissonConfigureProperties properties) {
        super(redissonBuildFactory.getConfig(), properties);
    }

    @Override
    public Object invoke(ChainInvoker invoker) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.MASTER_SLAVE.getPattern().equals(pattern)) {
            return invoker.proceed();
        }
        MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
        RedissonConfigureProperties.MasterSlaveConfig masterSlaveConfig = properties.getMasterSlaveConfig();
        //
        build(masterSlaveServersConfig, masterSlaveConfig);
        //
        String[] addressArr = masterSlaveConfig.getSlaveAddresses().split(BaseConstants.Symbol.COMMA);
        Arrays.asList(addressArr).forEach(address -> masterSlaveServersConfig.addSlaveAddress(addressFormat(address)));
        masterSlaveServersConfig.setDatabase(masterSlaveConfig.getDatabase())
                .setMasterAddress(addressFormat(masterSlaveConfig.getMasterAddress()))
                .setDnsMonitoringInterval(masterSlaveConfig.getDnsMonitoringInterval());
        //
        setLockSslConfig(masterSlaveServersConfig);
        return config;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
