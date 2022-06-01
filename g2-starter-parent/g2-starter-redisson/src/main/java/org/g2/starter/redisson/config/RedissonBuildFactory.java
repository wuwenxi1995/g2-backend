package org.g2.starter.redisson.config;

import org.g2.core.chain.Chain;
import org.g2.core.chain.invoker.base.BaseChainInvoker;
import org.g2.core.exception.CommonException;
import org.g2.core.helper.ApplicationContextHelper;
import org.g2.starter.redisson.config.responsibility.AbstractServerConfig;
import org.g2.starter.redisson.lock.config.RedissonConfigureProperties;
import org.g2.starter.redisson.lock.infra.constants.LockConstants;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.Collection;

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
        config.setCodec(new StringCodec());
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
        new RedissonClientAutoConfigureInvoker().proceed();
        return Redisson.create(config);
    }

    /**
     * 构建redisson客户端其他配置信息
     */
    private static class RedissonClientAutoConfigureInvoker extends BaseChainInvoker {

        @Override
        public Object proceed(Object... param) {
            try {
                return super.proceed(param);
            } catch (Exception e) {
                throw new CommonException(e);
            }
        }

        @Override
        protected Object invoke() {
            throw new CommonException("No suitable handler found");
        }

        @Override
        protected Collection<? extends Chain> initChain() {
            return ApplicationContextHelper.getApplicationContext().getBeansOfType(AbstractServerConfig.class).values();
        }
    }
}
