package org.g2.starter.core.thread.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务定时调度器
 *
 * @author wuwenxi 2021-08-16
 */
public class ScheduledTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private final String name;
    private final ScheduledExecutorService scheduler;
    private final ThreadPoolExecutor executor;
    private final Runnable task;
    private final long delay;
    private final boolean timeout;
    private final long timeoutMillis;

    private final AtomicInteger successCounter;
    private final AtomicInteger rejectedCounter;
    private final AtomicInteger throwableCounter;
    private final AtomicInteger timeoutCounter;

    public ScheduledTask(String name, ScheduledExecutorService scheduler, ThreadPoolExecutor executor, Runnable task, long delay, TimeUnit timeUnit, boolean timeout) {
        this.name = name;
        this.scheduler = scheduler;
        this.executor = executor;
        this.task = task;
        this.delay = timeUnit.toMillis(delay);
        this.timeout = timeout;
        this.timeoutMillis = timeout ? this.delay : 0;

        // Initialize the counters
        this.successCounter = new AtomicInteger(0);
        this.rejectedCounter = new AtomicInteger(0);
        this.throwableCounter = new AtomicInteger(0);
        this.timeoutCounter = new AtomicInteger(0);
    }

    @Override
    public void run() {
        Future<?> future = null;
        try {
            future = executor.submit(task);
            if (timeout) {
                future.get(timeoutMillis, TimeUnit.MILLISECONDS);
            } else {
                future.get();
            }
            successCounter.incrementAndGet();
        } catch (TimeoutException e) {
            logger.warn("scheduled task timed out", e);
            timeoutCounter.incrementAndGet();
        } catch (RejectedExecutionException e) {
            if (executor.isShutdown() || scheduler.isShutdown()) {
                logger.warn("scheduled task shutting down, reject the task", e);
            } else {
                logger.warn("scheduled task rejected the task", e);
            }
            rejectedCounter.incrementAndGet();
        } catch (Throwable e) {
            if (executor.isShutdown() || scheduler.isShutdown()) {
                logger.warn("scheduled task shutting down, can't accept the task");
            } else {
                logger.warn("scheduled task throw an exception", e);
            }
            throwableCounter.incrementAndGet();
        } finally {
            // 如果执行过程发生异常，FutureTask未完成，取消任务执行
            if (future != null && !future.isDone()) {
                future.cancel(true);
            }
            if (!scheduler.isShutdown()) {
                scheduler.schedule(this, delay, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public boolean cancel() {
        logger.info("scheduled task {} shutdown and execute success count : {} , timeout count :{} ,rejected count : {} , throwable count : {}",
                name, successCounter.get(), timeoutCounter.get(), rejectedCounter.get(), throwableCounter.get());
        return super.cancel();
    }
}
