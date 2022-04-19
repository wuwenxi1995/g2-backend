package org.g2.starter.redisson.delayed.annotation.producer;

import org.g2.core.helper.FastJsonHelper;
import org.g2.core.util.StringUtil;
import org.g2.starter.redisson.delayed.annotation.exception.DelayedQueueException;
import org.g2.starter.redisson.delayed.annotation.listener.DelayedMessageListenerContainer;
import org.g2.starter.redisson.delayed.annotation.listener.MessageListenerContainer;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2021-12-31
 */
public class DelayedMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(DelayedMessageProducer.class);
    private static final int DEFAULT_DELAY_TIME = 30;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    private final Map<String, RDelayedQueue<String>> delayedQueueMap;
    private final Map<String, MessageListenerContainer> messageListenerContainerMap;

    public DelayedMessageProducer() {
        this.delayedQueueMap = new ConcurrentHashMap<>();
        this.messageListenerContainerMap = new ConcurrentHashMap<>();
    }

    public boolean add(String queue, Object message, int delay, TimeUnit timeUnit) {
        RDelayedQueue<String> delayedQueue = getDelayedQueue(queue);
        try {
            delayedQueue.offer(FastJsonHelper.objectConvertString(message), delay, timeUnit);
            return true;
        } catch (Exception e) {
            log.error("redisson delayed queue add data error , msg : {}", StringUtil.exceptionString(e));
            return false;
        }
    }

    public boolean add(String queue, Object message) {
        return add(queue, message, DEFAULT_DELAY_TIME, DEFAULT_TIME_UNIT);
    }

    public void registryDelayedQueue(String queue, DelayedMessageListenerContainer messageListenerContainer) {
        this.delayedQueueMap.computeIfAbsent(queue, (key) -> {
            DelayedMessageListenerContainer.DelayedMessageTask delayedMessageTask = messageListenerContainer.getTask();
            RedissonClient redissonClient = delayedMessageTask.getRedissonClient();
            return redissonClient.getDelayedQueue(delayedMessageTask.getBlockingDeque());
        });
        this.messageListenerContainerMap.computeIfAbsent(queue, (key) -> messageListenerContainer);
    }

    public void destroy(String queue) {
        MessageListenerContainer messageListenerContainer = messageListenerContainerMap.get(queue);
        if (messageListenerContainer == null
                || messageListenerContainer.isRunning()) {
            return;
        }
        messageListenerContainerMap.remove(queue);
        // 销毁
        RDelayedQueue<String> delayedQueue = delayedQueueMap.remove(queue);
        if (delayedQueue == null) {
            return;
        }
        delayedQueue.destroy();
    }

    private RDelayedQueue<String> getDelayedQueue(String queue) {
        RDelayedQueue<String> delayedQueue = delayedQueueMap.get(queue);
        if (delayedQueue == null) {
            throw new DelayedQueueException("没有找到[%s]对应的延时队列", queue);
        }
        return delayedQueue;
    }
}
