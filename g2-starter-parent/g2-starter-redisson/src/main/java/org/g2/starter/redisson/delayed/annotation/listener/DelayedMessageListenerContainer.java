package org.g2.starter.redisson.delayed.annotation.listener;

import org.apache.commons.lang3.StringUtils;
import org.g2.core.util.StringUtil;
import org.g2.starter.redisson.delayed.annotation.adapter.MessageListenerAdapter;
import org.g2.starter.redisson.delayed.annotation.producer.DelayedMessageProducer;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2021-12-28
 */
public class DelayedMessageListenerContainer implements MessageListenerContainer {

    private static final Logger logger = LoggerFactory.getLogger(DelayedMessageListenerContainer.class);

    private final Object lifecycleMonitor = new Object();
    private final DelayedListenerProperties properties;
    private final DelayedMessageListenerContainer messageListenerContainer;

    private volatile boolean running;

    private Object messageListener;
    private DelayedMessageTask task;
    private Executor executor;

    public DelayedMessageListenerContainer(DelayedListenerProperties properties) {
        this.properties = properties;
        this.messageListenerContainer = this;
    }

    @Override
    public void setupListenerContainer(Object messageListener) {
        this.messageListener = messageListener;
    }

    public DelayedMessageTask getTask() {
        return task;
    }

    private void doStart() {
        if (isRunning()) {
            return;
        }
        running = true;
        this.task = new DelayedMessageTask();
        if (properties.getExecutor() != null) {
            this.executor = properties.getExecutor();
            this.executor.execute(task);
        } else {
            Executors.defaultThreadFactory().newThread(task).start();
        }
    }

    public class DelayedMessageTask implements Runnable {

        private String queue;
        private MessageListenerAdapter messageListener;
        private RedissonClient redissonClient;
        private RBlockingDeque<String> blockingDeque;
        private DelayedMessageProducer messageProducer;
        private Thread thread;

        DelayedMessageTask() {
            BeanFactory beanFactory = properties.getBeanFactory();
            this.queue = properties.getDelayedQueue();
            this.redissonClient = beanFactory.getBean(RedissonClient.class);
            this.blockingDeque = redissonClient.getBlockingDeque(queue);
            this.messageProducer = beanFactory.getBean(DelayedMessageProducer.class);
            messageProducer.registryDelayedQueue(this.queue, DelayedMessageListenerContainer.this.messageListenerContainer);
            if (DelayedMessageListenerContainer.this.messageListener instanceof MessageListenerAdapter) {
                this.messageListener = (MessageListenerAdapter) DelayedMessageListenerContainer.this.messageListener;
            }
            this.thread = Thread.currentThread();
        }

        public RedissonClient getRedissonClient() {
            return redissonClient;
        }

        public RBlockingDeque<String> getBlockingDeque() {
            return blockingDeque;
        }

        Thread getThread() {
            return thread;
        }

        @Override
        public void run() {
            while (isRunning()) {
                String message;
                try {
                    message = blockingDeque.take();
                } catch (Exception e) {
                    logger.error("redisson blocking queue has error , msg : {}", StringUtil.exceptionString(e));
                    continue;
                }
                if (StringUtils.isNotBlank(message)) {
                    if (thread.isInterrupted()) {
                        messageProducer.add(queue, message, 0, TimeUnit.SECONDS);
                        break;
                    }
                    if (executor != null) {
                        try {
                            executor.execute(() -> messageListener.handlerMessage(message));
                        } catch (RejectedExecutionException e) {
                            // 线程池拒绝任务，重新加入队列，等待消费
                            messageProducer.add(queue, message, 0, TimeUnit.SECONDS);
                        }
                    } else {
                        messageListener.handlerMessage(message);
                    }
                }
            }
            // 关闭延时队列
            messageProducer.destroy(this.queue);
        }
    }

    // 实现SmartLifecycle接口

    @Override
    public void start() {
        synchronized (this.lifecycleMonitor) {
            if (!isRunning()) {
                doStart();
            }
        }
    }

    @Override
    public void stop() {
        running = false;
        Thread taskThread = this.task.getThread();
        if (taskThread.isAlive()) {
            taskThread.interrupt();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
