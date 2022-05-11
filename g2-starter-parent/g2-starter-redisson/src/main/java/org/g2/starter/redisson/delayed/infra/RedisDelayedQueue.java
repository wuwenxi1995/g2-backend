package org.g2.starter.redisson.delayed.infra;

import org.apache.commons.lang3.StringUtils;
import org.g2.core.util.StringUtil;
import org.g2.starter.redisson.delayed.infra.listener.DelayedMessageListener;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author wuwenxi 2022-05-10
 */
@Component
public class RedisDelayedQueue {

    private static final Logger log = LoggerFactory.getLogger(RedisDelayedQueue.class);

    private final RedissonClient redissonClient;

    public RedisDelayedQueue(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <T> boolean offer(T data, long delayed, TimeUnit timeUnit, DelayedMessageListener<T> messageListener) {
        String queueName = StringUtils.defaultIfBlank(messageListener.queue(), messageListener.getClass().getSimpleName());
        return offer(data, delayed, timeUnit, queueName);
    }

    public <T> boolean offer(T data, long delayed, TimeUnit timeUnit, String queueName) {
        return operation(queueName, 0, (function) -> {
            try {
                function.offer(data, delayed, timeUnit);
                return true;
            } catch (Exception e) {
                log.error("add redis delayed queue error , msg : {}", StringUtil.exceptionString(e));
                return false;
            }
        });
    }

    public <T> boolean remove(T data, String queueName) {
        return operation(queueName, 0, (delayedQueue) -> {
            try {
                delayedQueue.remove(data);
            } catch (Exception e) {
                log.error("remove redis delayed queue error , msg : {}", StringUtil.exceptionString(e));
                return false;
            }
            return true;
        });
    }

    private <T> boolean operation(String queueName, int retry, Function<RDelayedQueue<T>, Boolean> function) {
        RDelayedQueue<T> delayedQueue = null;
        try {
            RBlockingDeque<T> blockingDeque = this.redissonClient.getBlockingDeque(queueName);
            delayedQueue = this.redissonClient.getDelayedQueue(blockingDeque);
            return function.apply(delayedQueue);
        } catch (Exception e) {
            log.error("operation Redisson delayed queue error, msg : {}", StringUtil.exceptionString(e));
            return retry == 0 && operation(queueName, 1, function);
        } finally {
            if (delayedQueue != null) {
                delayedQueue.destroy();
            }
        }
    }
}
