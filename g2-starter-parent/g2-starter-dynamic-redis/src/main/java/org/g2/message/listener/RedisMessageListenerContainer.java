package org.g2.message.listener;

import org.apache.commons.lang3.StringUtils;
import org.g2.core.thread.scheduler.ScheduledTask;
import org.g2.core.util.StringUtil;
import org.g2.message.config.properties.RedisMessageListenerProperties;
import org.g2.message.handler.MessageHandler;
import org.g2.message.repository.RedisQueueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2022-12-08
 */
public class RedisMessageListenerContainer implements SmartLifecycle, Runnable {

    private static final Logger log = LoggerFactory.getLogger(RedisMessageListenerContainer.class);

    private final int db;
    private final String queue;
    private MessageHandler messageHandler;
    private ApplicationContext applicationContext;
    private RedisQueueRepository redisQueueRepository;
    private RedisMessageListenerProperties properties;

    private volatile boolean isRunning;

    public RedisMessageListenerContainer(int db, String queue) {
        this.db = db;
        this.queue = queue;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setProperties(RedisMessageListenerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void run() {
        while (isRunning()) {
            Calendar now = Calendar.getInstance();
            String json = redisQueueRepository.pop(db, queue, now.getTimeInMillis(), expireTime(now));
            if (StringUtils.isNotEmpty(json)) {
                boolean success = true;
                try {
                    this.messageHandler.onMessage(json);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    log.error("");
                } catch (Exception e) {
                    log.error("redis message listener has error, listener: {}. errMsg: {}", this.getClass().getSimpleName(), StringUtil.exceptionString(e));
                    success = false;
                } finally {
                    if (properties.isAutoCommit() && success) {
                        redisQueueRepository.commit(db, queue, json);
                    } else if (!success) {
                        // 重试
                        redisQueueRepository.rollback(db, queue, json, properties.getRetry());
                    }
                }
            }
        }
    }

    @Override
    public void start() {
        this.isRunning = true;
        this.redisQueueRepository = applicationContext.getBean(RedisQueueRepository.class);
        ThreadPoolTaskScheduler scheduler = applicationContext.getBean("redisMessageListenerScheduler", ThreadPoolTaskScheduler.class);
        ThreadPoolTaskExecutor taskExecutor = applicationContext.getBean("redisMessageListenerExecutor", ThreadPoolTaskExecutor.class);
        ScheduledTask listenerConsumer = new ScheduledTask(messageHandler.getBean().getClass().getSimpleName(), scheduler.getScheduledExecutor(),
                taskExecutor.getThreadPoolExecutor(), this, properties.getDelayed().toMillis(), TimeUnit.MILLISECONDS, false);
        scheduler.schedule(listenerConsumer, new Date());
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    private long expireTime(Calendar now) {
        int rollbackTimes = (int) properties.getAutoRollbackTime().toMinutes();
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + rollbackTimes < 0 ? 5 : rollbackTimes);
        return now.getTimeInMillis();
    }
}
