package org.g2.core.thread;

import org.apache.commons.collections4.CollectionUtils;
import org.g2.core.helper.ApplicationContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 并发执行工具
 *
 * @author wuwenxi 2021-07-16
 */
public class ConcurrentExecutor<T> {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentExecutor.class);

    private static final int DEFAULT_TASK_COUNT = 5;
    private static final int DEFAULT_BATCH_SIZE = 400;
    private static final String THREAD_BEAN_NAME = "g2ThreadPool";

    private long taskCount;
    private long batchSize;
    private List<T> dataList;
    private ThreadPoolTaskExecutor threadPoolExecutor;
    private AtomicInteger taskCounter;

    public ConcurrentExecutor(List<T> dataList) {
        this(DEFAULT_TASK_COUNT, DEFAULT_BATCH_SIZE, dataList,
                ApplicationContextHelper.getApplicationContext().getBean(THREAD_BEAN_NAME, ThreadPoolTaskExecutor.class));
    }

    public ConcurrentExecutor(int taskCount, List<T> dataList) {
        this(taskCount, DEFAULT_BATCH_SIZE, dataList,
                ApplicationContextHelper.getApplicationContext().getBean(THREAD_BEAN_NAME, ThreadPoolTaskExecutor.class));
    }

    public ConcurrentExecutor(int taskCount, int batchSize, List<T> dataList) {
        this(taskCount, batchSize, dataList, ApplicationContextHelper.getApplicationContext().getBean(THREAD_BEAN_NAME, ThreadPoolTaskExecutor.class));
    }

    public ConcurrentExecutor(int taskCount, int batchSize, List<T> dataList, ThreadPoolTaskExecutor threadPoolExecutor) {
        this.taskCount = taskCount;
        this.batchSize = batchSize;
        this.dataList = dataList;
        this.threadPoolExecutor = threadPoolExecutor;
        this.taskCounter = new AtomicInteger(0);
    }

    public void invoke(final Consumer<T> consumer) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        if (taskCount <= 1 || dataList.size() < batchSize) {
            execute(consumer, dataList);
            return;
        }
        List<List<T>> splitList = org.g2.core.util.CollectionUtils.splitList(dataList, (int) batchSize);
        List<Future<?>> result = new ArrayList<>();
        for (List<T> tList : splitList) {
            result.add(submit(consumer, tList));
        }
        for (Future<?> future : result) {
            try {
                future.get();
            } catch (Exception e) {
                log.error("Concurrent Executor invoke error :", e);
            }
        }
    }

    private Future<?> submit(final Consumer<T> consumer, final List<T> dataList) {
        while (true) {
            int expect;
            if ((expect = taskCounter.get()) < taskCount) {
                int update = expect + 1;
                if (taskCounter.compareAndSet(expect, update)) {
                    return threadPoolExecutor.submit(() -> {
                        try {
                            execute(consumer, dataList);
                        } catch (Exception e) {
                            log.error("Concurrent Executor task execute error occured :", e);
                        } finally {
                            taskCounter.getAndDecrement();
                        }
                        return "success";
                    });
                }
            }
        }
    }

    private void execute(final Consumer<T> consumer, final List<T> dataList) {
        for (T data : dataList) {
            consumer.accept(data);
        }
    }
}
