package org.g2.message.listener;

import org.apache.commons.lang3.StringUtils;
import org.g2.core.util.StringUtil;
import org.g2.message.config.properties.RedisMessageListenerProperties;
import org.g2.message.handler.MessageHandler;
import org.g2.message.repository.RedisQueueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wuwenxi 2022-12-08
 */
public class RedisMessageListenerContainer implements SmartLifecycle, Runnable {

    private static final Logger log = LoggerFactory.getLogger(RedisMessageListenerContainer.class);

    private final int db;
    private final String queue;
    private MessageHandler messageHandler;
    private ApplicationContext applicationContext;
    private RedisMessageListenerProperties properties;
    private ThreadPoolTaskExecutor taskExecutor;

    private ListenableFuture<?> listenerConsumer;
    private ListenableFuture<?> ackListener;

    private AtomicBoolean polling = new AtomicBoolean();

    private volatile boolean running;

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

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void run() {
        RedisQueueRepository redisQueueRepository = applicationContext.getBean(RedisQueueRepository.class);
        while (isRunning()) {
            try {
                this.polling.set(true);
                String json = redisQueueRepository.poll(db, queue, properties.getPollTimeout());
                // 如果在poll的时候
                if (this.polling.compareAndSet(true, false) && StringUtils.isNotEmpty(json)) {
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
            } catch (Exception e) {
                CountDownLatch latch = new CountDownLatch(1);
                this.stop(latch::countDown);
                try {
                    latch.await(properties.getShutdownTimeout().toMillis(), TimeUnit.MILLISECONDS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void start() {
        if (!isRunning()) {
            this.running = true;
            this.listenerConsumer = taskExecutor.submitListenable(this);
            this.ackListener = taskExecutor.submitListenable(new AckListener());
        }
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        this.running = false;
        this.polling.set(false);
    }

    @Override
    public void stop(Runnable callback) {
        if (isRunning()) {
            listenerConsumer.addCallback(new StopCallback(callback));
            stop();
            ackListener.addCallback(new ListenableFutureCallback<Object>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.error("Error while stopping the Ack Listener", ex);
                }

                @Override
                public void onSuccess(Object result) {
                    log.debug("Ack Listener stopped normal");
                }
            });
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }


    private class AckListener implements Runnable {

        @Override
        public void run() {
            RedisQueueRepository redisQueueRepository = applicationContext.getBean(RedisQueueRepository.class);
            while (isRunning()) {
                boolean process = redisQueueRepository.ackTimeout(db, queue, properties.getAckTimeout());
                if (process) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(properties.getPollTimeout().toMillis());
                    } catch (InterruptedException e) {
                        log.error("Ack Listener : [" + this + "] has been interrupted", e);
                    }
                }
            }
        }
    }

    private class StopCallback implements ListenableFutureCallback<Object> {
        private final Runnable callback;

        StopCallback(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable ex) {
            RedisMessageListenerContainer.log.error("Error while stopping the container: ", ex);
            if (this.callback != null) {
                this.callback.run();
            }
        }

        @Override
        public void onSuccess(Object result) {
            RedisMessageListenerContainer.log.debug(RedisMessageListenerContainer.this + "stopped normally");
            if (this.callback != null) {
                this.callback.run();
            }
        }
    }
}
