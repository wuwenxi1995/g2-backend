package org.g2.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 多线程调度工具
 *
 * @author wenxi.wu@hand-chian.com 2020-11-26
 */
public final class ThreadPoolExecutorUtil {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolExecutorUtil.class);

    private ThreadPoolExecutorUtil() {
    }

    public static <T> void runTask(List<T> data, int maxThread, int threadSize, ThreadPoolTaskExecutor executor, Consumer<List<T>> consumer) {
        List<List<T>> splitList = CollectionUtils.splitList(data, threadSize);
        if (splitList == null) {
            return;
        }
        if (splitList.size() == 1) {
            consumer.accept(data);
        } else {
            runTask(splitList, maxThread, executor, consumer);
        }
    }

    public static <T> void runTask(List<T> data, int maxThread, ThreadPoolTaskExecutor executor, Consumer<T> consumer) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(data.size());
        AtomicInteger taskCounter = new AtomicInteger(0);
        List<Future<String>> result = new ArrayList<>();
        data.forEach(processData -> result.add(taskRunner(processData, executor, maxThread, taskCounter, consumer)));

        for (Future<String> future : result) {
            try {
                future.get();
            } catch (Exception e) {
                log.error("");
            } finally {
                countDownLatch.countDown();
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("");
        }
    }

    private static <T> Future<String> taskRunner(T processData, ThreadPoolTaskExecutor executor, int maxThread, AtomicInteger taskCounter, Consumer<T> consumer) {
        Callable<String> taskRunner = () -> {
            taskCounter.incrementAndGet();
            try {
                consumer.accept(processData);
            } catch (Exception e) {
                log.error("");
            } finally {
                taskCounter.decrementAndGet();
            }
            return "success";
        };
        while (true) {
            if (taskCounter.get() < maxThread) {
                return executor.submit(taskRunner);
            }
        }
    }
}
