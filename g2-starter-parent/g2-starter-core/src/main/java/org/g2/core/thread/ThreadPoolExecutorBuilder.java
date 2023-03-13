package org.g2.core.thread;

import org.g2.core.util.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2023-03-03
 */
public class ThreadPoolExecutorBuilder {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolExecutorBuilder.class);

    public static ThreadPoolExecutor build(String poolName) {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        return build(corePoolSize, corePoolSize << 2, 1, TimeUnit.MINUTES, 65536, poolName);
    }

    public static ThreadPoolExecutor build(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int capacity, String poolName) {
        DecorateExecutor executor = new DecorateExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, capacity, poolName);
        ExecutorManager.registryManager(poolName, executor);
        return executor;
    }

    public static class ExecutorManager {
        private static final Map<String, ExecutorService> EXECUTORS = new ConcurrentHashMap<>(16);

        private static void registryManager(String poolName, ExecutorService executorService) {
            EXECUTORS.put(poolName, executorService);
            hookShutdown(executorService, poolName);
        }

        public static ExecutorService getExecutorService(String poolName) {
            return EXECUTORS.get(poolName);
        }

        public static Map<String, ExecutorService> getAllExecutorService() {
            return new HashMap<>(EXECUTORS);
        }

        public static void removeExecutor(String poolName) {
            EXECUTORS.remove(poolName);
        }

        private static void hookShutdown(ExecutorService executorService, String poolName) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("[>>ExecutorShutdown<<<] start shutdown the threadPool :{}", poolName);
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                    try {
                        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                            log.warn("[>>ExecutorShutdown<<<] Interrupt the worker, which may cause some task inconsistent. Please check the biz logs.");
                            executorService.shutdownNow();
                            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                                log.error("[>>ExecutorShutdown<<<] Thread pool can't be shutdown even with interrupting worker threads, which may cause some task inconsistent. Please check the biz logs.");
                            }
                        }
                    } catch (InterruptedException e) {
                        executorService.shutdownNow();
                        log.error("[>>ExecutorShutdown<<] The current server thread is interrupted when it is trying to stop the worker threads. This may leave an inconsistent state. Please check the biz logs.");
                        Thread.currentThread().interrupt();
                    }
                }
            }));
        }
    }

    private static class DecorateExecutor extends ThreadPoolExecutor {

        DecorateExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int capacity, String poolName) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new DecorateBlockingQueue(capacity));
            ((DecorateBlockingQueue) getQueue()).setExecutor(this);
            this.setThreadFactory(new ThreadFactoryBuilder().setNameFormat(poolName).setUncaughtExceptionHandler((t, ex) -> {
                log.error("{} catching the uncaught exception, thread:{}", poolName, t, ex);
            }).build());
            this.setRejectedExecutionHandler((runnable, executor) -> {
                try {
                    getQueue().put(runnable);
                } catch (InterruptedException e) {
                    log.warn("{} queue offer interrupted", poolName, e);
                    Thread.currentThread().interrupt();
                }
            });
            this.allowCoreThreadTimeOut(true);
        }
    }

    private static class DecorateBlockingQueue extends LinkedBlockingQueue<Runnable> {

        private ThreadPoolExecutor executor;

        void setExecutor(ThreadPoolExecutor executor) {
            this.executor = executor;
        }

        DecorateBlockingQueue(int capacity) {
            super(capacity);
        }

        @Override
        public void put(@NonNull Runnable runnable) throws InterruptedException {
            if (executor.isShutdown()) {
                throw new RejectedExecutionException("Executor [" + executor.toString() + "] is shutdown, offer task [" + runnable.toString() + "] failed");
            }
            super.put(runnable);
        }

        @Override
        public boolean offer(@NonNull Runnable runnable) {
            return false;
        }
    }

    private int coreSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n < 0 ? 1 : n + 1;
    }
}
