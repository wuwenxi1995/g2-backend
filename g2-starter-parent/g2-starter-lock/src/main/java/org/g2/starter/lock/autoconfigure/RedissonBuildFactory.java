package org.g2.starter.lock.autoconfigure;

import org.g2.core.exception.CommonException;
import org.g2.core.handler.MethodInvocationHandler;
import org.g2.core.handler.impl.ChainInvocationHandler;
import org.g2.starter.lock.config.RedissonConfigureProperties;
import org.g2.starter.lock.infra.constants.LockConstants;
import org.g2.starter.lock.infra.responsibility.AbstractServerConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author wuwenxi 2021-07-07
 */
public class RedissonBuildFactory {

    private final RedissonConfigureProperties properties;
    private final Config config;

    public RedissonBuildFactory(RedissonConfigureProperties properties) {
        this.properties = properties;
        config = new Config();
    }

    private void init() {
        config.setTransportMode(LockConstants.TransportMode.getTransportMode(properties.getTransportMode()));
        config.setThreads(properties.getThreads());
        config.setNettyThreads(properties.getNettyThreads());
        config.setKeepPubSubOrder(properties.isKeepPubSubOrder());
        config.setLockWatchdogTimeout(properties.getLockWatchdogTimeout());
        config.setUseScriptCache(properties.isUseScriptCache());
    }

    public Config getConfig() {
        return config;
    }

    RedissonClient build() {
        // 生成其他配置信息
        new RedissonClientAutoConfigureHandler().proceed();
        return Redisson.create(config);
    }

    /**
     * 构建redisson客户端其他配置信息
     */
    private static class RedissonClientAutoConfigureHandler extends ChainInvocationHandler {

        @Override
        public Object proceed() {
            try {
                return super.proceed();
            } catch (Exception e) {
                throw new CommonException(e);
            }
        }

        @Override
        protected Object invoke() {
            throw new CommonException("No suitable handler found");
        }

        @Override
        protected Class<? extends MethodInvocationHandler> beanType() {
            return AbstractServerConfig.class;
        }
    }
}
