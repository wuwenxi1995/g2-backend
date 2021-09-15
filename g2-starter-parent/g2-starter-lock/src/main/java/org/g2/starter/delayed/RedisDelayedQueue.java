package org.g2.starter.delayed;

import org.g2.core.util.ThreadFactoryBuilder;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wuwenxi 2021-09-15
 */
public abstract class RedisDelayedQueue<T> implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(RedisDelayedQueue.class);

    @Autowired
    private RedissonClient redissonClient;

    private RBlockingDeque<T> blockingDeque;
    private RDelayedQueue<T> delayedQueue;

    /**
     * 线程池用来提交任务，默认创建自定义线程池，推荐使用公用线程池
     *
     * @see RedisDelayedQueue#createExecutorService()
     * @see RedisDelayedQueue#setExecutorService(java.util.concurrent.ExecutorService)
     */
    private ExecutorService executorService;

    /**
     * delayedQueue数据处理的工作状态
     */
    private AtomicReference<Status> status;

    /**
     * 数据入队
     *
     * @param data 数据
     * @return true/false
     */
    public final boolean add(T data) {
        return add(data, delayedTime(), timeUnit());
    }

    public final boolean add(T data, int delayed, TimeUnit timeUnit) {
        try {
            delayedQueue.offer(data, delayed, timeUnit);
        } catch (Exception e) {
            log.error("add redis delayed queue error , msg :", e);
            return false;
        }
        return true;
    }

    /**
     * 处理任务
     */
    public final void processing() {
        Assert.notNull(executorService, "require executorService not null");
        if (!status.compareAndSet(Status.PENDING, Status.RUNNING)) {
            throw new IllegalStateException("RedisDelayedQueue processing status is RUNNING");
        }
        while (!Thread.currentThread().isInterrupted()
                && !executorService.isShutdown()) {
            try {
                T take = blockingDeque.take();
                // 如果线程池已关闭，说明服务不可用，将数据重新放入阻塞队列
                if (executorService.isShutdown() || Thread.currentThread().isInterrupted()) {
                    delayedQueue.offer(take, 0, timeUnit());
                    break;
                } else {
                    // 提交任务
                    executorService.submit(() -> process(take));
                }
            } catch (Exception e) {
                log.error("redis delayed queue processing error , msg :", e);
            }
        }
        // gc
        status = null;
    }

    protected void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 延迟时间
     *
     * @return time
     */
    protected int delayedTime() {
        return 30;
    }

    /**
     * TimeUnit
     *
     * @return timeUnit
     */
    protected TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(redissonClient,"require redissonClient not null");
        String queue = queue();
        Assert.isTrue(StringUtils.hasText(queue), "delayedQueue require queue not null");
        this.blockingDeque = redissonClient.getBlockingDeque(queue);
        this.delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        // 创建
        if (executorService == null) {
            this.executorService = createExecutorService();
        }
        status = new AtomicReference<>(Status.PENDING);
    }

    @PreDestroy
    private void destroy() {
        delayedQueue.destroy();
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    private ExecutorService createExecutorService() {
        int threads = Runtime.getRuntime().availableProcessors() * 2;
        return new ThreadPoolExecutor(threads, threads, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(999),
                new ThreadFactoryBuilder().setNameFormat(String.format("%s-", this.getClass().getSimpleName())).build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 数据处理
     *
     * @param data 数据
     */
    protected abstract void process(T data);

    /**
     * 延时队列名
     *
     * @return 队列名
     */
    protected abstract String queue();

    enum Status {
        /**
         * 执行状态
         */
        PENDING,
        RUNNING;
    }
}
