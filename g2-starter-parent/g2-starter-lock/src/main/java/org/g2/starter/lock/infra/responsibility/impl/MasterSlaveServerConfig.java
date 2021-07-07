package org.g2.starter.lock.infra.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.handler.InvocationHandler;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.infra.enums.ServerPattern;
import org.g2.starter.lock.infra.responsibility.AbstractServerConfig;
import org.redisson.config.Config;
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

    public MasterSlaveServerConfig(Config config, RedissonConfigureProperties properties) {
        super(config, properties);
    }

    @Override
    public Object invoke(InvocationHandler handler) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.MASTER_SLAVE.getPattern().equals(pattern)) {
            return handler.proceed();
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
