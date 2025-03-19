package org.g2.starter.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    /**
     * 不拆分数组，一个线程执行一个数据
     *
     * @param data      执行数据
     * @param maxThread 最大线程数
     * @param executor  线程池
     * @param consumer  执行函数
     * @param <T>       数据类型
     */
    public static <T> void runTask(List<T> data, int maxThread, ThreadPoolTaskExecutor executor, Consumer<T> consumer) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(data.size());
        AtomicInteger taskCounter = new AtomicInteger(0);
        List<Future<String>> result = new ArrayList<>();
        data.forEach(processData -> result.add(taskRunner(processData, executor, maxThread, taskCounter, consumer)));
        // 等待调度结束
        for (Future<String> future : result) {
            try {
                future.get();
            } catch (Exception e) {
                log.error("ThreadPoolExecutorUtil thread happened error : ", e);
            } finally {
                countDownLatch.countDown();
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error(" ThreadPoolExecutorUtil blocking wait exception : ", e);
        }
    }

    /**
     * 拆分数组，一个线程执行多个数据
     * 适用于数据量大的多线程任务
     *
     * @param data       执行数据
     * @param maxThread  最大线程数
     * @param threadSize 拆分的每个数组大小
     * @param executor   线程池
     * @param consumer   执行函数
     * @param <T>        数据类型
     */
    public static <T> void runTask(List<T> data, int maxThread, int threadSize, ThreadPoolTaskExecutor executor, Consumer<List<T>> consumer) {
        if (data.size() < threadSize) {
            consumer.accept(data);
            return;
        }
        List<List<T>> splitList = CollectionUtils.splitList(data, threadSize);
        runTask(splitList, maxThread, executor, consumer);
    }

    /**
     * 数据分组,一个线程执行多组数据
     *
     * @param data      执行数据
     * @param maxThread 最大线程数
     * @param executor  线程池
     * @param consumer  执行函数
     * @param <E>       key
     * @param <T>       value
     */
    public static <E, T> void runTask(Map<E, T> data, int maxThread, int threadSize, ThreadPoolTaskExecutor executor, Consumer<Map<E, T>> consumer) {
        if (MapUtils.isEmpty(data)) {
            return;
        }
        if (data.size() < threadSize) {
            consumer.accept(data);
            return;
        }
        List<Map<E, T>> mapList = MapUtils.splitMap(data, threadSize);
        runTask(mapList, maxThread, executor, consumer);
    }

    /**
     * 数据分组,一个线程执行一组数据
     *
     * @param data      执行数据
     * @param maxThread 最大线程数
     * @param executor  线程池
     * @param consumer  执行函数
     * @param <E>       key
     * @param <T>       value
     */
    public static <E, T> void runTask(Map<E, T> data, int maxThread, ThreadPoolTaskExecutor executor, Consumer<Map<E, T>> consumer) {
        if (MapUtils.isEmpty(data)) {
            return;
        }
        if (data.size() == 1) {
            consumer.accept(data);
            return;
        }
        runTask(data, maxThread, 1, executor, consumer);
    }

    private static <T> Future<String> taskRunner(T processData, ThreadPoolTaskExecutor executor, int maxThread, AtomicInteger taskCounter, Consumer<T> consumer) {
        Callable<String> taskRunner = () -> {
            try {
                consumer.accept(processData);
            } catch (Exception e) {
                log.error("ThreadPoolExecutorUtil taskRunner error occur : ", e);
            } finally {
                taskCounter.decrementAndGet();
            }
            return "success";
        };
        while (true) {
            int expect;
            if ((expect = taskCounter.get()) < maxThread) {
                int update = expect + 1;
                if (taskCounter.compareAndSet(expect, update)) {
                    return executor.submit(taskRunner);
                }
            }
        }
    }
}
