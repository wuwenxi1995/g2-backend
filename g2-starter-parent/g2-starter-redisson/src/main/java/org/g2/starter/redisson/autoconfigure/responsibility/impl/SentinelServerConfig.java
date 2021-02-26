package org.g2.starter.redisson.autoconfigure.responsibility.impl;

import org.g2.core.base.BaseConstants;
import org.g2.core.handler.InvocationHandler;
import org.g2.starter.redisson.autoconfigure.responsibility.AbstractServerConfig;
import org.g2.starter.redisson.config.LockConfigureProperties;
import org.g2.starter.redisson.infra.constants.LockConstants;
import org.g2.starter.redisson.infra.enums.ServerPattern;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;

import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * redis哨兵配置
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class SentinelServerConfig extends AbstractServerConfig {

    public SentinelServerConfig(Config config, LockConfigureProperties properties) {
        super(config, properties);
    }

    @Override
    public Object invoke(InvocationHandler handler) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.SENTINEL.getPattern().equals(pattern)) {
            return handler.proceed();
        }
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
        LockConfigureProperties.SentinelConfig sentinelConfig = properties.getSentinelConfig();
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
