package org.g2.starter.redisson.autoconfigure.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.handler.InvocationHandler;
import org.g2.starter.redisson.autoconfigure.responsibility.AbstractServerConfig;
import org.g2.starter.redisson.config.LockConfigureProperties;
import org.g2.starter.redisson.infra.enums.ServerPattern;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;

import java.util.Arrays;

/**
 * redis主从配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class MasterSlaveServerConfig extends AbstractServerConfig {

    public MasterSlaveServerConfig(Config config, LockConfigureProperties properties) {
        super(config, properties);
    }

    @Override
    public Object invoke(InvocationHandler handler) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.MASTER_SLAVE.getPattern().equals(pattern)) {
            return handler.proceed();
        }
        MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
        LockConfigureProperties.MasterSlaveConfig masterSlaveConfig = properties.getMasterSlaveConfig();
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
}
