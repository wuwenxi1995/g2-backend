package org.g2.message.listener.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuwenxi 2022-12-08
 */
public class RedisMessageListenerEndpointRegistry implements SmartLifecycle, ApplicationContextAware,
        ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, RedisMessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();

    private ConfigurableApplicationContext applicationContext;

    private boolean contextRefreshed;

    private volatile boolean running;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().equals(this.applicationContext)) {
            this.contextRefreshed = true;
        }
    }

    @Override
    public void start() {
        for (RedisMessageListenerContainer listenerContainer : getListenerContainers()) {
            startIfNecessary(listenerContainer);
        }
        this.running = true;
    }

    @Override
    public void stop() {
        this.running = false;
        for (RedisMessageListenerContainer listenerContainer : getListenerContainers()) {
            listenerContainer.stop();
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    /**
     * 创建监听器
     */
    public void registerListenerContainer(MethodRedisMessageListenerEndpoint endpoint, RedisMessageListenerContainerFactory factory, boolean startImmediately) {
        Assert.notNull(endpoint, "Endpoint must be set");
        Assert.notNull(factory, "Factory must be set");
        String queueName = endpoint.getQueueName();
        Assert.hasText(queueName, "Endpoint queue must be set");
        synchronized (this.listenerContainers) {
            Assert.state(!listenerContainers.containsKey(queueName), "Another endpoint is already registered with id '"
                    + queueName + "'");
            RedisMessageListenerContainer container = createListenerContainer(endpoint, factory);
            listenerContainers.put(queueName, container);
            if (startImmediately) {
                startIfNecessary(container);
            }
        }
    }

    private RedisMessageListenerContainer createListenerContainer(MethodRedisMessageListenerEndpoint endpoint, RedisMessageListenerContainerFactory factory) {
        return null;
    }

    private void startIfNecessary(RedisMessageListenerContainer listenerContainer) {
        if (this.contextRefreshed || listenerContainer.isAutoStartup()) {
            listenerContainer.start();
        }
    }

    private Collection<RedisMessageListenerContainer> getListenerContainers() {
        return Collections.unmodifiableCollection(this.listenerContainers.values());
    }

}
