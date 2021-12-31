package org.g2.starter.redisson.delayed.annotation.registry;

import org.g2.starter.redisson.delayed.annotation.DelayedListener;
import org.g2.starter.redisson.delayed.annotation.listener.DelayedListenerProperties;
import org.g2.starter.redisson.delayed.annotation.listener.DelayedMessageListenerContainer;
import org.g2.starter.redisson.delayed.annotation.listener.MessageListenerContainer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuwenxi 2021-12-27
 */
public class DelayedListenerEndpointRegistry implements SmartLifecycle, ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, MessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();

    private volatile boolean running;
    private boolean contextRefresh;

    public void registerListenerContainer(DelayedListenerEndpoint endpoint, Executor executor) {
        registerListenerContainer(endpoint, executor, false);
    }

    public void registerListenerContainer(DelayedListenerEndpoint endpoint, Executor executor, boolean startImmediately) {
        Assert.notNull(endpoint, "Endpoint must not be null");
        String id = endpoint.getId();
        Assert.hasText(id, "Endpoint id must not be empty");
        synchronized (this.listenerContainers) {
            Assert.state(!this.listenerContainers.containsKey(id),
                    "Another endpoint is already registered with id '" + id + "'");
            MessageListenerContainer container = createListenerContainer(endpoint, executor);
            this.listenerContainers.put(id, container);
            if (startImmediately) {
                startIfNecessary(container);
            }
        }
    }

    private void startIfNecessary(MessageListenerContainer container) {
        if (this.contextRefresh || container.isAutoStartup()) {
            container.start();
        }
    }

    private MessageListenerContainer createListenerContainer(DelayedListenerEndpoint endpoint, Executor executor) {
        MessageListenerContainer listenerContainer = new DelayedMessageListenerContainer(createProperties(endpoint, executor));
        endpoint.setupListenerContainer(listenerContainer);
        return listenerContainer;
    }

    private DelayedListenerProperties createProperties(DelayedListenerEndpoint endpoint, Executor executor) {
        DelayedListenerProperties properties = new DelayedListenerProperties();
        DelayedListener delayedListener = endpoint.getDelayedListener();
        Assert.state(delayedListener != null, "DelayedListener must not be null");
        properties.setDelayedQueue(delayedListener.queue());
        properties.setDelayed(delayedListener.delayed());
        properties.setTimeUnit(delayedListener.timeUnit());
        properties.setTarget(endpoint.getBean());
        properties.setMethod(endpoint.getMethod());
        properties.setTargetName(endpoint.getBeanName());
        properties.setBeanFactory(endpoint.getBeanFactory());
        if (executor != null) {
            properties.setExecutor(executor);
        }
        return properties;
    }

    //  实现SmartLifecycle接口

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        this.running = false;
        Collection<MessageListenerContainer> listenerContainersToStop = this.listenerContainers.values();
        if (listenerContainersToStop.size() > 0) {
            AggregatingCallback aggregatingCallback = new AggregatingCallback(listenerContainersToStop.size(), callback);
            for (MessageListenerContainer listenerContainer : listenerContainersToStop) {
                if (listenerContainer.isRunning()) {
                    listenerContainer.stop(aggregatingCallback);
                } else {
                    aggregatingCallback.run();
                }
            }
        } else {
            callback.run();
        }
    }

    @Override
    public void start() {
        this.running = true;
        for (MessageListenerContainer listenerContainer : listenerContainers.values()) {
            listenerContainer.start();
        }
    }

    @Override
    public void stop() {
        this.running = false;
        for (MessageListenerContainer listenerContainer : listenerContainers.values()) {
            listenerContainer.stop();
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    // 实现ApplicationListener接口

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        this.contextRefresh = true;
    }

    private static final class AggregatingCallback implements Runnable {

        private final AtomicInteger count;

        private final Runnable finishCallback;

        private AggregatingCallback(int count, Runnable finishCallback) {
            this.count = new AtomicInteger(count);
            this.finishCallback = finishCallback;
        }

        @Override
        public void run() {
            if (this.count.decrementAndGet() <= 0) {
                this.finishCallback.run();
            }
        }
    }
}
