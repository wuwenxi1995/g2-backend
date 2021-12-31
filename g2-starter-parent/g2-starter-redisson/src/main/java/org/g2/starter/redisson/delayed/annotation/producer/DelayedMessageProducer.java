package org.g2.starter.redisson.delayed.annotation.producer;

import org.g2.core.helper.FastJsonHelper;
import org.g2.core.util.StringUtil;
import org.g2.starter.redisson.delayed.annotation.exception.DelayedQueueException;
import org.g2.starter.redisson.delayed.annotation.listener.DelayedMessageListenerContainer;
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

    public DelayedMessageProducer() {
        this.delayedQueueMap = new ConcurrentHashMap<>();
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

    public void registryDelayedQueue(String queue, DelayedMessageListenerContainer.DelayedMessageTask delayedMessageTask) {
        this.delayedQueueMap.computeIfPresent(queue, (key, oldValue) -> {
            RedissonClient redissonClient = delayedMessageTask.getRedissonClient();
            return redissonClient.getDelayedQueue(delayedMessageTask.getBlockingDeque());
        });
    }

    private RDelayedQueue<String> getDelayedQueue(String queue) {
        RDelayedQueue<String> delayedQueue = delayedQueueMap.get(queue);
        if (delayedQueue == null) {
            throw new DelayedQueueException("没有找到[%s]对应的延时队列", queue);
        }
        return delayedQueue;
    }

    public void destroy(String queue) {
        // 销毁
        RDelayedQueue<String> delayedQueue = delayedQueueMap.remove(queue);
        if (delayedQueue == null) {
            return;
        }
        delayedQueue.destroy();
    }
}
