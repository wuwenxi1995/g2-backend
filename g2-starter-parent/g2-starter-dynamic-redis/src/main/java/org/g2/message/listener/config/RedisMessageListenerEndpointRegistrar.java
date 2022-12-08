package org.g2.message.listener.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuwenxi 2022-12-08
 */
public class RedisMessageListenerEndpointRegistrar implements InitializingBean {

    private final Map<MethodRedisMessageListenerEndpoint, RedisMessageListenerContainerFactory> endpoints = new HashMap<>();
    private RedisMessageListenerEndpointRegistry endpointRegistry;

    private boolean startImmediately;

    public void setEndpointRegistry(RedisMessageListenerEndpointRegistry endpointRegistry) {
        this.endpointRegistry = endpointRegistry;
    }

    @Override
    public void afterPropertiesSet() {
        synchronized (this.endpoints) {
            for (Map.Entry<MethodRedisMessageListenerEndpoint, RedisMessageListenerContainerFactory> entry : this.endpoints.entrySet()) {
                this.endpointRegistry.registerListenerContainer(entry.getKey(), entry.getValue(), false);
            }
            this.startImmediately = true;
        }
    }

    public void registerEndpoint(MethodRedisMessageListenerEndpoint endpoint, RedisMessageListenerContainerFactory containerFactory) {
        Assert.notNull(endpoint, "Endpoint must be set");
        Assert.hasText(endpoint.getQueueName(), "Endpoint queue must be set");
        synchronized (this.endpoints) {
            if (this.startImmediately) {
                endpointRegistry.registerListenerContainer(endpoint, containerFactory, true);
            } else {
                this.endpoints.put(endpoint, containerFactory);
            }
        }
    }
}
