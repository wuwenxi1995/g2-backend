package org.g2.starter.core.thread.task.producer;

import org.g2.starter.core.thread.task.client.ProducerClient;
import org.g2.starter.core.thread.task.consumer.TaskConsumer;
import org.springframework.util.Assert;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 任务生产者
 *
 * @author wuwenxi 2021-08-02
 */
public abstract class TaskProducerClient<T> implements TaskConsumer.Producer<T>, ProducerClient {

    private BlockingQueue<T> blockingQueue;
    private TaskManager taskManager;
    private ThreadPoolExecutor threadPoolExecutor;
    private AtomicReference<TaskConsumer.Status> status;

    public TaskProducerClient(ThreadPoolExecutor threadPoolExecutor, int period, TimeUnit timeUnit, int coreTaskSize, int queueCapacity) {
        this.blockingQueue = new LinkedBlockingQueue<>(queueCapacity);
        this.taskManager = new TaskManager(threadPoolExecutor, blockingQueue, period, timeUnit, coreTaskSize);
        this.threadPoolExecutor = threadPoolExecutor;
        this.status = new AtomicReference<>(TaskConsumer.Status.INACTIVE);
    }

    @Override
    public void add(T data) {
        final TaskConsumer.Status currentStatus = this.status.get();
        Assert.isTrue(currentStatus == TaskConsumer.Status.ACTIVE, String.format("execute failed;Task Producer Client status: %s", currentStatus));
        blockingQueue.add(data);
    }

    @Override
    public void start() {
        if (status.compareAndSet(TaskConsumer.Status.INACTIVE, TaskConsumer.Status.ACTIVE)) {
            threadPoolExecutor.submit(taskManager);
        }
    }

    @Override
    public void stop() {
        if (status.compareAndSet(TaskConsumer.Status.ACTIVE, TaskConsumer.Status.STOPPED)) {
            taskManager.stop();
            // help GC;
            taskManager = null;
            blockingQueue = null;
            status = null;
        }
    }

    public class TaskManager extends TaskConsumer<T> {
        private Integer period;
        private TimeUnit timeUnit;

        TaskManager(ExecutorService executorService, BlockingQueue<T> blockingQueue, Integer period, TimeUnit timeUnit, Integer coreTaskSize) {
            super(executorService, blockingQueue, coreTaskSize);
            this.period = period;
            this.timeUnit = timeUnit;
        }

        @Override
        protected void doFinally(ScheduledThreadPoolExecutor scheduledExecutor) {
            if (!scheduledExecutor.isShutdown()) {
                scheduledExecutor.scheduleAtFixedRate(this, period, 0, timeUnit);
            }
        }

        @Override
        protected void handler(T data) {
            process(data);
        }
    }

    /**
     * 数据处理
     *
     * @param data 数据处理
     */
    protected abstract void process(T data);
}
