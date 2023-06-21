package org.g2.starter.redisson.delayed.infra;

import org.apache.commons.lang3.StringUtils;
import org.g2.starter.core.util.StringUtil;
import org.g2.starter.redisson.delayed.infra.listener.DelayedMessageListener;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RDestroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2022-05-10
 */
public class RedisDelayedQueue {

    private static final Logger log = LoggerFactory.getLogger(RedisDelayedQueue.class);

    private final Map<String, RDelayedQueue<?>> delayedQueueMap;

    public RedisDelayedQueue() {
        this.delayedQueueMap = new ConcurrentHashMap<>();
    }

    public void register(RDelayedQueue<?> delayedQueue) {
        delayedQueueMap.putIfAbsent(delayedQueue.getName(), delayedQueue);
    }

    public void destroy() {
        synchronized (this) {
            if (delayedQueueMap.size() > 0) {
                delayedQueueMap.values().forEach(RDestroyable::destroy);
                delayedQueueMap.clear();
            }
        }
    }

    public <T> boolean offer(T data, long delayed, TimeUnit timeUnit, DelayedMessageListener<T> messageListener) {
        String queueName = StringUtils.defaultIfBlank(messageListener.queue(), messageListener.getClass().getSimpleName());
        return offer(data, delayed, timeUnit, queueName);
    }

    @SuppressWarnings("unchecked")
    public <T> boolean offer(T data, long delayed, TimeUnit timeUnit, String queueName) {
        RDelayedQueue delayedQueue = delayedQueueMap.get(queueName);
        Objects.requireNonNull(delayedQueue, String.format("delayedQueue not found, queueName : %s", queueName));
        try {
            delayedQueue.offer(data, delayed, timeUnit);
            return true;
        } catch (Exception e) {
            log.error("add redis delayed queue error , msg : {}", StringUtil.exceptionString(e));
            return false;
        }
    }

    public <T> boolean remove(T data, String queueName) {
        RDelayedQueue<?> delayedQueue = delayedQueueMap.get(queueName);
        Objects.requireNonNull(delayedQueue, String.format("delayedQueue not found, queueName : %s", queueName));
        try {
            delayedQueue.remove(data);
        } catch (Exception e) {
            log.error("remove redis delayed queue error , msg : {}", StringUtil.exceptionString(e));
            return false;
        }
        return true;
    }
}
