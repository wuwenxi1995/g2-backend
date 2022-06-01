package org.g2.starter.redisson.delayed.infra.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.g2.core.task.TaskHandler;
import org.g2.core.util.ThreadFactoryBuilder;
import org.g2.starter.redisson.delayed.infra.RedisDelayedQueue;
import org.g2.starter.redisson.delayed.infra.listener.DelayedMessageListener;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2022-05-10
 */
public class DelayedQueueHandler extends TaskHandler {
    private static final Logger log = LoggerFactory.getLogger(DelayedQueueHandler.class);

    private ThreadPoolExecutor executor;
    private List<Thread> workers;

    private final RedissonClient redissonClient;
    private final RedisDelayedQueue redisDelayedQueue;

    public DelayedQueueHandler(RedissonClient redissonClient, RedisDelayedQueue redisDelayedQueue) {
        this.redissonClient = redissonClient;
        this.redisDelayedQueue = redisDelayedQueue;
    }

    @Override
    protected void run() {
        this.workers.forEach(Thread::start);
    }

    @Override
    protected void doStart() {
        Map<String, DelayedMessageListener> beans = getApplicationContext().getBeansOfType(DelayedMessageListener.class);
        if (beans.size() < 1) {
            return;
        }
        this.workers = new ArrayList<>(beans.size());
        for (DelayedMessageListener<?> messageListener : beans.values()) {
            String queueName = StringUtils.defaultIfBlank(messageListener.queue(), messageListener.getClass().getSimpleName());
            Thread thread = Executors.defaultThreadFactory().newThread(new DelayedQueueTask<>(messageListener, queueName));
            thread.setName("delayed-worker-" + queueName);
            this.workers.add(thread);
        }
        this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                20,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(999),
                new ThreadFactoryBuilder().setNameFormat("delayed-queue-%s").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    protected void doStop() {
        if (CollectionUtils.isNotEmpty(workers)) {
            for (Thread worker : workers) {
                if (worker.isAlive()) {
                    worker.interrupt();
                }
            }
        }
        if (executor != null && !executor.isShutdown()) {
            this.executor.shutdown();
        }
        redisDelayedQueue.destroy();
    }

    private class DelayedQueueTask<T> implements Runnable {

        private final DelayedMessageListener<T> delayedMessageListener;
        private final String queueName;
        private final RBlockingDeque<T> blockingDeque;

        private DelayedQueueTask(DelayedMessageListener<T> delayedMessageListener, String queueName) {
            this.delayedMessageListener = delayedMessageListener;
            this.queueName = queueName;
            this.blockingDeque = redissonClient.getBlockingDeque(queueName);
            //
            redisDelayedQueue.register(redissonClient.getDelayedQueue(blockingDeque));
        }

        @Override
        public void run() {
            while (isRunning()) {
                T take = null;
                try {
                    take = blockingDeque.take();
                    // 如果当前任务结束,或任务线程被中断,将数据重新放回延时队列中
                    if (!isRunning() || Thread.currentThread().isInterrupted()) {
                        reset(take);
                        break;
                    }
                    T message = take;
                    executor.submit(() -> {
                        // 线程池关闭线程被中断,将数据重新放回延时队列中
                        if (Thread.currentThread().isInterrupted()) {
                            reset(message);
                            return;
                        }
                        delayedMessageListener.onMessage(message);
                    });
                } catch (InterruptedException e) {
                    // take()被中断，记录错误日志
                    log.error("take blockingDeque Interrupted, msg :", e);
                } catch (RejectedExecutionException e) {
                    // 提交线程池被拒绝，重新放入阻塞队列
                    reset(take);
                    log.error("delayedQueueTask submit task rejected, msg : ", e);
                } catch (Throwable t) {
                    // 如果发生异常，记录错误日志
                    log.error("delayedQueueTask has error , msg :", t);
                }
            }
        }

        private void reset(T message) {
            redisDelayedQueue.offer(message, 0, TimeUnit.NANOSECONDS, queueName);
        }
    }
}
