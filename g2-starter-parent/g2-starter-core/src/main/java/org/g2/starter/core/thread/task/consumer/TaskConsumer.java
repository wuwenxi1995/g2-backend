package org.g2.starter.core.thread.task.consumer;

import org.g2.starter.core.thread.task.client.ConsumerClient;
import org.g2.starter.core.util.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务消费者
 *
 * @author wuwenxi 2021-08-02
 */
public class TaskConsumer<T> implements Runnable, ConsumerClient {

    private static final Logger log = LoggerFactory.getLogger(TaskConsumer.class);

    /**
     * 正在工作的优先级队列
     */
    private PriorityQueue<Task> workingQueue;
    /**
     * 暂停的工作队列
     */
    private LinkedList<Task> idleQueue;

    private ReentrantLock reentrantLock;

    private ExecutorService executorService;
    private ScheduledThreadPoolExecutor scheduledExecutor;
    private BlockingQueue<T> blockingQueue;
    private Integer coreTaskSize;

    public TaskConsumer(ExecutorService executorService, BlockingQueue<T> blockingQueue, Integer coreTaskSize) {
        this.executorService = executorService;
        this.scheduledExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setDaemon(true).build());
        this.blockingQueue = blockingQueue;
        this.workingQueue = new PriorityQueue<>();
        this.idleQueue = new LinkedList<>();
        this.reentrantLock = new ReentrantLock(true);
        this.coreTaskSize = coreTaskSize;

    }

    @Override
    public void run() {
        try {
            if (blockingQueue.size() == 0) {
                while (workingQueue.size() > 1) {
                    reassignOneTeller();
                }
                return;
            }
            if (blockingQueue.size() >> 1 > workingQueue.size()) {
                if (idleQueue.size() > 0) {
                    Task task = idleQueue.remove();
                    task.resume();
                    workingQueue.offer(task);
                } else if (workingQueue.size() < coreTaskSize) {
                    Task task = new Task(blockingQueue, reentrantLock);
                    executorService.submit(task);
                    workingQueue.offer(task);
                }
            } else if (workingQueue.size() > 1
                    && (blockingQueue.size() >> 1 < workingQueue.size())) {
                reassignOneTeller();
            }
        } catch (Exception e) {
            log.error("Task Manager execute error:", e);
        } finally {
            doFinally(scheduledExecutor);
        }
    }

    @Override
    public final void stop() {
        // 唤醒全部工作线程
        while (idleQueue.size() > 0) {
            Task task = idleQueue.remove();
            task.resume();
            workingQueue.offer(task);
        }
        // 阻塞等待数据处理完成
        for (; ; ) {
            if (blockingQueue.size() == 0) {
                break;
            }
        }
        while (workingQueue.size() > 0) {
            Task task = workingQueue.poll();
            task.stop();
        }
        // help GC;
        workingQueue = null;
        idleQueue = null;
        reentrantLock = null;
        scheduledExecutor.shutdown();
    }

    private void reassignOneTeller() {
        Task task = workingQueue.poll();
        if (task != null) {
            task.suspend();
            idleQueue.offer(task);
        }
    }

    protected void doFinally(ScheduledThreadPoolExecutor scheduledExecutor) {
        if (!scheduledExecutor.isShutdown()) {
            scheduledExecutor.scheduleAtFixedRate(this, 10, 0, TimeUnit.SECONDS);
        }
    }

    protected void handler(T data) {
    }

    private class Task implements Runnable, Comparable<Task> {

        private int customerServerCount = 0;
        private boolean servingCustomer = true;

        private BlockingQueue<T> blockingQueue;
        private Condition stop;

        Task(BlockingQueue<T> blockingQueue, ReentrantLock reentrantLock) {
            this.blockingQueue = blockingQueue;
            this.stop = reentrantLock.newCondition();
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    T take = blockingQueue.take();
                    handler(take);
                    customerServerCount++;
                    while (!servingCustomer) {
                        stop.await();
                    }
                }
            } catch (Exception e) {
                log.error("Task execute error:", e);
            } finally {
                workingQueue.remove(this);
            }
        }

        final void suspend() {
            servingCustomer = false;
            customerServerCount = 0;
        }

        final void resume() {
            servingCustomer = true;
            stop.signal();
        }

        final void stop() {
            if (!Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public synchronized int compareTo(Task other) {
            return Integer.compare(this.customerServerCount, other.customerServerCount);
        }
    }

    /**
     * 生产者
     */
    @FunctionalInterface
    public interface Producer<T> {
        /**
         * 新增数据
         *
         * @param data 数据
         */
        void add(T data);
    }

    /**
     * 监控状态
     */
    public enum Status {
        /**
         * 初始化
         */
        INACTIVE,
        /**
         * 工作
         */
        ACTIVE,
        /**
         * 停止
         */
        STOPPED;
    }
}
