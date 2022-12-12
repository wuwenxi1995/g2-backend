package org.g2.message.config.endpoint;

import org.g2.message.listener.RedisMessageListenerContainer;
import org.g2.message.config.properties.RedisMessageListenerProperties;
import org.g2.message.handler.MessageHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2022-12-08
 */
public class RedisMessageListenerEndpointRegistry implements SmartLifecycle,
        ApplicationContextAware,
        ApplicationListener<ApplicationReadyEvent> {

    private final Map<String, RedisMessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();

    private ConfigurableApplicationContext applicationContext;

    private boolean contextRefreshed;

    private final Object lifecycleMonitor = new Object();

    private volatile boolean running;

    @Autowired
    private RedisMessageListenerProperties properties;

    @Resource(name = "redisMessageListenerExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        }
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        this.contextRefreshed = true;
        for (RedisMessageListenerContainer listenerContainer : this.getListenerContainers()) {
            startIfNecessary(listenerContainer);
        }
    }

    /**
     * 创建监听器
     */
    public void registerListenerContainer(MethodRedisMessageListenerEndpoint endpoint, boolean startImmediately) {
        Assert.notNull(endpoint, "Endpoint must be set");
        String queueName = endpoint.getQueueName();
        Assert.hasText(queueName, "Endpoint queue must be set");
        synchronized (this.listenerContainers) {
            Assert.state(!listenerContainers.containsKey(queueName), "Another endpoint is already registered with id '"
                    + queueName + "'");
            RedisMessageListenerContainer container = createListenerContainer(endpoint);
            listenerContainers.put(queueName, container);
            if (startImmediately) {
                startIfNecessary(container);
            }
        }
    }

    private RedisMessageListenerContainer createListenerContainer(MethodRedisMessageListenerEndpoint endpoint) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer(endpoint.getDb(), endpoint.getQueueName());
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.setBean(endpoint.getBean());
        messageHandler.setMethod(endpoint.getMethod());
        messageHandler.setType(endpoint.getType());
        listenerContainer.setMessageHandler(messageHandler);
        listenerContainer.setApplicationContext(applicationContext);
        listenerContainer.setProperties(properties);
        listenerContainer.setTaskExecutor(taskExecutor);
        return listenerContainer;
    }

    private void startIfNecessary(RedisMessageListenerContainer listenerContainer) {
        if (this.contextRefreshed || listenerContainer.isAutoStartup()) {
            listenerContainer.start();
        }
    }

    private Collection<RedisMessageListenerContainer> getListenerContainers() {
        return Collections.unmodifiableCollection(this.listenerContainers.values());
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        this.running = true;
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        this.running = false;
    }

    @Override
    public void stop(Runnable callback) {
        synchronized (this.lifecycleMonitor) {
            if (isRunning()) {
                stop();
                for (RedisMessageListenerContainer listenerContainer : getListenerContainers()) {
                    CountDownLatch latch = new CountDownLatch(1);
                    listenerContainer.stop(new StopCallback(latch, callback));
                    try {
                        latch.await(properties.getShutdownTimeout().toMillis(), TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private static class StopCallback implements Runnable {
        private final CountDownLatch latch;
        private final Runnable callback;

        StopCallback(CountDownLatch latch, Runnable callback) {
            this.latch = latch;
            this.callback = callback;
        }

        @Override
        public void run() {
            latch.countDown();
            callback.run();
        }
    }
}
