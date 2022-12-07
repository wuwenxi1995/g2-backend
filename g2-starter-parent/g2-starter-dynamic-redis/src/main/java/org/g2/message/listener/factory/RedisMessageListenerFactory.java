package org.g2.message.listener.factory;

import org.apache.commons.lang3.StringUtils;
import org.g2.core.helper.FastJsonHelper;
import org.g2.core.task.TaskHandler;
import org.g2.core.thread.scheduler.ScheduledTask;
import org.g2.core.util.StringUtil;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.message.listener.RedisMessageListener;
import org.g2.message.listener.annotation.Listener;
import org.g2.message.listener.repository.RedisQueueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuwenxi 2022-12-06
 */
public class RedisMessageListenerFactory extends TaskHandler {

    private static final Logger log = LoggerFactory.getLogger(RedisMessageListenerFactory.class);

    private final Map<String, RedisMessageListener<?>> messageListenerMap;
    private List<ScheduledTask> scheduledTasks = new ArrayList<>();

    @Resource(name = "redisMessageListenerScheduler")
    private ThreadPoolTaskScheduler taskScheduler;

    @Resource(name = "redisMessageListenerExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    public RedisMessageListenerFactory() {
        this.messageListenerMap = new ConcurrentHashMap<>();
    }

    public void add(String beanName, RedisMessageListener<?> redisMessageListener) {
        messageListenerMap.put(beanName, redisMessageListener);
    }

    @Override
    protected void run() {
        this.scheduledTasks.forEach(ScheduledTask::run);
    }

    @Override
    protected void doStart() {
        if (!messageListenerMap.isEmpty()) {
            messageListenerMap.forEach((beanName, redisMessageListener) -> {
                Listener listener = redisMessageListener.getClass().getAnnotation(Listener.class);
                if (listener != null) {
                    RedisMessageListenerTask<?> listenerTask = new RedisMessageListenerTask<>(listener.queue(), redisMessageListener, listener);
                    ScheduledTask scheduledTask = new ScheduledTask(beanName, taskScheduler.getScheduledExecutor(), taskExecutor.getThreadPoolExecutor(),
                            listenerTask, listener.delayed(), listener.timeUnit(), false);
                    this.scheduledTasks.add(scheduledTask);
                }
            });
        }
    }

    @Override
    protected void doStop() {
        this.scheduledTasks.forEach(ScheduledTask::cancel);
    }

    @SuppressWarnings("unchecked")
    private class RedisMessageListenerTask<T> implements Runnable {

        private final String queue;
        private final RedisMessageListener<T> redisMessageListener;
        private final Listener listener;
        private final Class<T> type;

        RedisMessageListenerTask(String queue, RedisMessageListener<T> redisMessageListener, Listener listener) {
            this.queue = queue;
            this.redisMessageListener = redisMessageListener;
            this.listener = listener;
            ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
            this.type = (Class) parameterizedType.getActualTypeArguments()[0];
        }

        @Override
        public void run() {
            RedisQueueRepository redisQueueRepository = getApplicationContext().getBean(RedisQueueRepository.class);
            while (isRunning()) {
                Calendar now = Calendar.getInstance();
                String json = redisQueueRepository.pop(listener.db(), queue, now.getTimeInMillis(), expireTime(now));
                if (StringUtils.isEmpty(json)) {
                    break;
                }
                T message;
                if (type.isAssignableFrom(String.class)) {
                    message = (T) json;
                } else {
                    message = FastJsonHelper.stringConvertObject(json, type);
                }
                boolean success = true;
                try {
                    this.redisMessageListener.onMessage(message);
                } catch (Exception e) {
                    log.error("redis message listener has error, listener: {}. errMsg: {}", this.getClass().getSimpleName(), StringUtil.exceptionString(e));
                    success = false;
                } finally {
                    if (listener.isAutoCommit() && success) {
                        redisQueueRepository.commit(listener.db(), queue, json);
                    } else if (!success) {
                        // 重试
                        redisQueueRepository.rollback(listener.db(), queue, json);
                    }
                }
            }
        }

        private long expireTime(Calendar now) {
            now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + listener.rollbackTimeM() < 0 ? 5 : listener.rollbackTimeM());
            return now.getTimeInMillis();
        }
    }
}
