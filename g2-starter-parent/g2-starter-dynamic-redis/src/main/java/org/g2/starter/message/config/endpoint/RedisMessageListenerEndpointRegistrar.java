package org.g2.starter.message.config.endpoint;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuwenxi 2022-12-08
 */
public class RedisMessageListenerEndpointRegistrar implements InitializingBean {

    private final List<MethodRedisMessageListenerEndpoint> endpoints = new ArrayList<>();
    private RedisMessageListenerEndpointRegistry endpointRegistry;

    private boolean startImmediately;

    public void setEndpointRegistry(RedisMessageListenerEndpointRegistry endpointRegistry) {
        this.endpointRegistry = endpointRegistry;
    }

    @Override
    public void afterPropertiesSet() {
        synchronized (this.endpoints) {
            for (MethodRedisMessageListenerEndpoint endpoint : this.endpoints) {
                this.endpointRegistry.registerListenerContainer(endpoint, false);
            }
            this.startImmediately = true;
        }
    }

    public void registerEndpoint(MethodRedisMessageListenerEndpoint endpoint) {
        Assert.notNull(endpoint, "Endpoint must be set");
        Assert.hasText(endpoint.getQueueName(), "Endpoint queue must be set");
        synchronized (this.endpoints) {
            if (this.startImmediately) {
                endpointRegistry.registerListenerContainer(endpoint, true);
            } else {
                this.endpoints.add(endpoint);
            }
        }
    }
}
