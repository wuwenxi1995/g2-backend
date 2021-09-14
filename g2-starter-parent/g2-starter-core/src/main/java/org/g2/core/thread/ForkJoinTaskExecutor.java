package org.g2.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * @author wuwenxi 2021-08-20
 */
public class ForkJoinTaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(ForkJoinTaskExecutor.class);

    public static ForkJoinPool instance() {
        return ForkJoinPool.commonPool();
    }

    public static <T> void submit(Runnable runnable, T result) {
        ForkJoinTask<?> submit = instance().submit(runnable, result);
        try {
            submit.get();
        } catch (Exception e) {
            log.error("fork/join submit error : ", e);
        }
    }

    public static <T> void execute(List<T> data, int maxSize, Function<Collection<T>, Void> function) {
        Task<T, Void> task = new Task<>(data, maxSize, function);
        ForkJoinTask<Void> submit = instance().submit(task);
        try {
            submit.get();
        } catch (Exception e) {
            log.error("fork/join execute error : ", e);
        }
    }

    public static <T> void execute(List<T> data, int maxSize, Function<Collection<T>, Void> function, int timeoutMillis) throws TimeoutException {
        Task<T, Void> task = new Task<>(data, maxSize, function);
        ForkJoinTask<Void> submit = instance().submit(task);
        try {
            submit.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            log.error("fork/join execute error : ", e);
        }
    }

    private static class Task<T, E> extends RecursiveAction {

        private final List<T> data;
        private final int maxSize;
        private final Function<Collection<T>, E> function;

        Task(List<T> data, int maxSize, Function<Collection<T>, E> function) {
            this.data = data;
            this.maxSize = maxSize;
            this.function = function;
        }

        @Override
        protected void compute() {
            if (data.size() < maxSize) {
                E apply = function.apply(data);
                return;
            }
            int middle = data.size() >>> 1;

            Task<T, E> left = new Task<>(data.subList(0, middle), maxSize, function);
            Task<T, E> right = new Task<>(data.subList(middle + 1, data.size()), maxSize, function);
            invokeAll(Arrays.asList(left, right));
        }
    }
}
