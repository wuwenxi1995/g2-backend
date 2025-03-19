package org.g2.starter.redisson.config.responsibility.impl;

import org.apache.commons.lang3.StringUtils;
import org.g2.starter.core.base.BaseConstants;
import org.g2.starter.core.chain.invoker.ChainInvoker;
import org.g2.starter.redisson.config.RedissonBuildFactory;
import org.g2.starter.redisson.config.responsibility.AbstractServerConfig;
import org.g2.starter.redisson.lock.config.RedissonConfigureProperties;
import org.g2.starter.redisson.lock.infra.constants.LockConstants;
import org.g2.starter.redisson.lock.infra.enums.ServerPattern;
import org.redisson.config.SslProvider;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Component
public class SingleServerConfig extends AbstractServerConfig {

    public SingleServerConfig(RedissonBuildFactory redissonBuildFactory, RedissonConfigureProperties properties) {
        super(redissonBuildFactory.getConfig(), properties);
    }

    @Override
    public Object invoke(ChainInvoker invoker) throws Exception {
        String pattern = properties.getPattern();
        if (!ServerPattern.SINGLE.getPattern().equals(pattern)) {
            return invoker.proceed();
        }
        org.redisson.config.SingleServerConfig singleServerConfig = config.useSingleServer();
        // 构建单节点配置
        RedissonConfigureProperties.SingleConfig singleConfig = properties.getSingleConfig();
        String address = addressFormat("%s%s%s%s", LockConstants.REDIS_URL_PREFIX, singleConfig.getAddress(), BaseConstants.Symbol.COLON, singleConfig.getPort());
        singleServerConfig.setAddress(address);
        singleServerConfig.setClientName(properties.getClientName());

        singleServerConfig.setConnectionMinimumIdleSize(singleConfig.getConnMinIdleSize());
        singleServerConfig.setConnectionPoolSize(singleConfig.getConnPoolSize());
        singleServerConfig.setConnectTimeout((int) singleConfig.getConnTimeout());
        singleServerConfig.setDatabase(singleConfig.getDatabase());
        singleServerConfig.setDnsMonitoringInterval(singleConfig.getDnsMonitoringInterval());
        singleServerConfig.setIdleConnectionTimeout((int) singleConfig.getIdleConnTimeout());
        singleServerConfig.setKeepAlive(singleConfig.isKeepAlive());
        if (StringUtils.isNotBlank(singleConfig.getPassword())) {
            singleServerConfig.setPassword(singleConfig.getPassword());
        }
        singleServerConfig.setRetryAttempts(singleConfig.getRetryAttempts());
        singleServerConfig.setRetryInterval((int) singleConfig.getRetryInterval());
        singleServerConfig.setSslEnableEndpointIdentification(properties.isSslEnableEndpointIdentification());
        if (StringUtils.isNotBlank(properties.getSslKeystore())) {
            singleServerConfig.setSslKeystore(new URI(properties.getSslKeystore()));
        }

        if (StringUtils.isNotBlank(properties.getSslKeystorePassword())) {
            singleServerConfig.setSslKeystorePassword(properties.getSslKeystorePassword());
        }

        singleServerConfig.setSslProvider("JDK".equalsIgnoreCase(properties.getSslProvider()) ? SslProvider.JDK : SslProvider.OPENSSL);
        return config;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
