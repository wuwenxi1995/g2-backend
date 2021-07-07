package org.g2.starter.lock.infra.responsibility;

import org.apache.commons.lang3.StringUtils;
import org.g2.core.base.BaseConstants;
import org.g2.core.chain.handler.ChainInvocationHandler;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.infra.constants.LockConstants;
import org.redisson.config.BaseMasterSlaveServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SslProvider;
import org.redisson.config.SubscriptionMode;
import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.connection.balancer.RandomLoadBalancer;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.redisson.connection.balancer.WeightedRoundRobinBalancer;
import org.springframework.util.Assert;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public abstract class AbstractServerConfig implements ServerConfig, ChainInvocationHandler {

    public Config config;
    protected RedissonConfigureProperties properties;

    public AbstractServerConfig(Config config, RedissonConfigureProperties properties) {
        this.config = config;
        this.properties = properties;
    }

    @Override
    public void build(BaseMasterSlaveServersConfig config, RedissonConfigureProperties.BaseConfig baseConfig) {
        ReadMode readMode = readMode(baseConfig.getReadMode());
        Assert.notNull(readMode, "Unknown load balancing mode type for read operations");
        config.setReadMode(readMode);
        SubscriptionMode subscriptionMode = subscriptionMode(baseConfig.getSubscriptionMode());
        Assert.notNull(subscriptionMode, "The type of load balancing pattern for an unknown subscription operation");
        config.setSubscriptionMode(subscriptionMode);
        LoadBalancer loadBalancer = loadBalancer(baseConfig);
        Assert.notNull(loadBalancer, "Unknown type of load balancing algorithm");
        config.setLoadBalancer(loadBalancer);
        config.setSubscriptionConnectionMinimumIdleSize(baseConfig.getSubscriptionConnectionMinimumIdleSize());
        config.setSubscriptionConnectionPoolSize(baseConfig.getSubscriptionConnectionPoolSize());
        config.setSlaveConnectionMinimumIdleSize(baseConfig.getSlaveConnectionMinimumIdleSize());
        config.setSlaveConnectionPoolSize(baseConfig.getSlaveConnectionPoolSize());
        config.setMasterConnectionMinimumIdleSize(baseConfig.getMasterConnectionMinimumIdleSize());
        config.setMasterConnectionPoolSize(baseConfig.getMasterConnectionPoolSize());
        config.setIdleConnectionTimeout((int) baseConfig.getIdleConnectionTimeout());
        config.setConnectTimeout((int) baseConfig.getConnectTimeout());
        config.setTimeout((int) baseConfig.getTimeout());
        config.setRetryAttempts(baseConfig.getRetryAttempts());
        config.setRetryInterval(baseConfig.getRetryInterval());
        if (StringUtils.isNotBlank(baseConfig.getPassword())) {
            config.setPassword(baseConfig.getPassword());
        }
        config.setSubscriptionsPerConnection(baseConfig.getSubscriptionsPerConnection());
        config.setClientName(properties.getClientName());
    }

    @Override
    public void setLockSslConfig(BaseMasterSlaveServersConfig config) throws URISyntaxException {
        if (properties.isSslEnableEndpointIdentification()) {
            config.setSslEnableEndpointIdentification(properties.isSslEnableEndpointIdentification());
            if (properties.getSslKeystore() != null) {
                config.setSslKeystore(new URI(properties.getSslKeystore()));
            }
            if (properties.getSslKeystorePassword() != null) {
                config.setSslKeystorePassword(properties.getSslKeystorePassword());
            }
            if (properties.getSslTruststore() != null) {
                config.setSslTruststore(new URI(properties.getSslTruststore()));
            }
            if (properties.getSslTruststorePassword() != null) {
                config.setSslTruststorePassword(properties.getSslTruststorePassword());
            }
            config.setSslProvider(LockConstants.JDK.equals(properties.getSslProvider()) ? SslProvider.JDK : SslProvider.OPENSSL);
        }
    }

    protected String addressFormat(String format, Object... args) {
        return String.format(format, args);
    }

    protected String addressFormat(String address) {
        return addressFormat("%s%s", LockConstants.REDIS_URL_PREFIX, address);
    }

    private ReadMode readMode(String readMode) {
        return LockConstants.SubReadMode.SLAVE.equals(readMode) ? ReadMode.SLAVE : (LockConstants.SubReadMode.MASTER.equals(readMode) ? ReadMode.MASTER : (LockConstants.SubReadMode.MASTER_SLAVE.equals(readMode) ? ReadMode.MASTER_SLAVE : null));
    }

    private SubscriptionMode subscriptionMode(String subscriptionMode) {
        return LockConstants.SubReadMode.SLAVE.equals(subscriptionMode) ? SubscriptionMode.SLAVE : (LockConstants.SubReadMode.MASTER.equals(subscriptionMode) ? SubscriptionMode.MASTER : null);
    }

    private LoadBalancer loadBalancer(RedissonConfigureProperties.BaseConfig baseConfig) {
        String loadBalancer = baseConfig.getLoadBalancer();
        switch (loadBalancer) {
            case LockConstants.LoadBalancer.ROUND_ROBIN_LOAD_BALANCER:
                return new RoundRobinLoadBalancer();
            case LockConstants.LoadBalancer.RANDOM_LOAD_BALANCER:
                return new RandomLoadBalancer();
            case LockConstants.LoadBalancer.WEIGHTED_ROUND_ROBIN_BALANCER:
                Map<String, Integer> weights = new HashMap<>(16);
                String[] weightMaps = baseConfig.getWeightMaps().split(BaseConstants.Symbol.SEMICOLON);
                Arrays.asList(weightMaps).forEach((weightMap) -> {
                    String[] split = weightMap.split(BaseConstants.Symbol.COLON);
                    weights.put(LockConstants.REDIS_URL_PREFIX + split[0], Integer.parseInt(split[1]));
                });
                return new WeightedRoundRobinBalancer(weights, baseConfig.getDefaultWeight());
            default:
                return null;
        }
    }
}
