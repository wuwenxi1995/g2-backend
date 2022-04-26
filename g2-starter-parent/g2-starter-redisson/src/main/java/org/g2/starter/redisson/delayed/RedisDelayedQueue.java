package org.g2.starter.redisson.delayed;

import org.g2.core.task.TaskHandler;
import org.g2.core.util.ThreadFactoryBuilder;
import org.g2.starter.redisson.delayed.config.properties.DelayedQueueProperties;
import org.g2.starter.redisson.delayed.infra.RedisDelayedRepository;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author wuwenxi 2021-09-15
 */
public abstract class RedisDelayedQueue<T> extends TaskHandler implements RedisDelayedRepository<T> {

    private static final Logger log = LoggerFactory.getLogger(RedisDelayedQueue.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private DelayedQueueProperties properties;

    private RBlockingDeque<T> blockingDeque;
    private boolean isUseExecutor = true;

    /**
     * 线程池用来提交任务，默认创建自定义线程池，推荐使用公用线程池
     *
     * @see RedisDelayedQueue#setExecutorService(java.util.concurrent.ExecutorService)
     */
    private ExecutorService executorService;

    @Override
    public final boolean add(T data) {
        return add(data, delayedTime(), timeUnit());
    }

    @Override
    public boolean add(T data, int delayed, TimeUnit timeUnit) {
        return operation((delayedQueue) -> {
            try {
                delayedQueue.offer(data, delayed, timeUnit);
            } catch (Exception e) {
                log.error("add redis delayed queue error , msg :", e);
                return false;
            }
            return true;
        });
    }

    @Override
    public boolean remove(T data) {
        return operation((delayedQueue) -> {
            try {
                delayedQueue.remove(data);
            } catch (Exception e) {
                log.error("remove redis delayed queue error , msg :", e);
                return false;
            }
            return true;
        });
    }

    private boolean operation(Function<RDelayedQueue<T>, Boolean> function) {
        RDelayedQueue<T> delayedQueue = this.redissonClient.getDelayedQueue(blockingDeque);
        try {
            return function.apply(delayedQueue);
        } finally {
            delayedQueue.destroy();
        }
    }

    @Override
    protected void run() {
        while (isRunning()) {
            T take = null;
            try {
                take = blockingDeque.poll(300, TimeUnit.SECONDS);
                if (Objects.isNull(take)) {
                    continue;
                }
                // 如果线程中断，说明服务不可用，将数据重新放入阻塞队列
                if (!isRunning() || Thread.currentThread().isInterrupted()) {
                    add(take, 0, timeUnit());
                    break;
                }
                // 处理数据
                if (isUseExecutor) {
                    if (executorService.isShutdown()) {
                        add(take, 0, timeUnit());
                        break;
                    }
                    T submit = take;
                    executorService.submit(() -> process(submit));
                } else {
                    process(take);
                }
            } catch (InterruptedException e) {
                // take()被中断，记录错误日志
                log.error("take delayedQueue Interrupted, msg :", e);
            } catch (RejectedExecutionException e) {
                // 提交线程池被拒绝，重新放入阻塞队列
                add(take, 0, timeUnit());
                log.error("delayedQueue submit task rejected, msg : ", e);
            } catch (Throwable t) {
                // 如果发生异常，记录错误日志
                log.error("RedisDelayedQueue has error , msg :", t);
            }
        }
    }

    @Override
    protected void doStart() {
        // 先添加一条数据
        add(null, 0, TimeUnit.SECONDS);
        this.isUseExecutor = properties.getExecutor().isEnable();
        if (isUseExecutor && this.executorService == null) {
            setExecutorService(createExecutorService(properties.getExecutor()));
        }
    }

    @Override
    protected void doStop() {
        if (isUseExecutor && !executorService.isShutdown()) {
            executorService.shutdown();
        }
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
        Assert.notNull(redissonClient, "require redissonClient not null");
        String queue = queue();
        Assert.isTrue(StringUtils.hasText(queue), "delayedQueue require queue not null");
        this.blockingDeque = redissonClient.getBlockingDeque(queue);
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

    private ExecutorService createExecutorService(DelayedQueueProperties.Executor executor) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(executor.getCoreSize(), executor.getMaxSize(), executor.getKeepAliveSecond(),
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(executor.getQueueCapacity()),
                new ThreadFactoryBuilder().setNameFormat(String.format("%s-", "delayedQueue")).build(),
                new ThreadPoolExecutor.AbortPolicy());
        executorService.allowCoreThreadTimeOut(executor.isAllowCoreThreadTimeOut());
        return executorService;
    }
}
