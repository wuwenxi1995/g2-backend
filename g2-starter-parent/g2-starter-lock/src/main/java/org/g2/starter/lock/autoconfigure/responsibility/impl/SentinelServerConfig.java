package org.g2.starter.lock.autoconfigure.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.handler.InvocationHandler;
import org.g2.starter.lock.autoconfigure.responsibility.AbstractServerConfig;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.infra.constants.LockConstants;
import org.g2.starter.lock.infra.enums.ServerPattern;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;

import java.util.Arrays;

/**
 * redis哨兵配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class SentinelServerConfig extends AbstractServerConfig {

    public SentinelServerConfig(Config config, RedissonConfigureProperties properties) {
        super(config, properties);
    }

    @Override
    public Object invoke(InvocationHandler handler) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.SENTINEL.getPattern().equals(pattern)) {
            return handler.proceed();
        }
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
        RedissonConfigureProperties.SentinelConfig sentinelConfig = properties.getSentinelConfig();
        build(sentinelServersConfig, sentinelConfig);
        sentinelServersConfig.setMasterName(sentinelConfig.getMasterName())
                .setDatabase(sentinelConfig.getDatabase())
                .setDnsMonitoringInterval(sentinelConfig.getDnsMonitoringInterval());
        setLockSslConfig(sentinelServersConfig);
        String[] addressArr = sentinelConfig.getSentinelAddresses().split(BaseConstants.Symbol.COMMA);
        Arrays.asList(addressArr).forEach(address -> sentinelServersConfig.addSentinelAddress(String.format("%s%s", LockConstants.REDIS_URL_PREFIX, address)));
        return config;
    }
}
