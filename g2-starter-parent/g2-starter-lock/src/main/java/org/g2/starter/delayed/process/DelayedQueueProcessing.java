package org.g2.starter.delayed.process;

import org.apache.commons.collections4.CollectionUtils;
import org.g2.core.util.ThreadFactoryBuilder;
import org.g2.starter.delayed.RedisDelayedQueue;
import org.g2.starter.delayed.config.properties.DelayedQueueProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2021-09-15
 */
public class DelayedQueueProcessing implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DelayedQueueProperties properties;

    private List<Thread> delayedQueueThreadList = new ArrayList<>();

    @Override
    public void run(String... args) throws Exception {
        Map<String, RedisDelayedQueue> beansOfType = applicationContext.getBeansOfType(RedisDelayedQueue.class);
        if (CollectionUtils.isNotEmpty(beansOfType.values())) {
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
            boolean enableExecutor = properties.getExecutor().isEnable();
            ExecutorService executorService = null;
            for (RedisDelayedQueue<?> redisDelayedQueue : beansOfType.values()) {
                // 先添加一条数据
                redisDelayedQueue.add(null, 0, TimeUnit.SECONDS);
                if (enableExecutor) {
                    if (executorService == null) {
                        executorService = createExecutorService(properties.getExecutor());
                    }
                    redisDelayedQueue.setExecutorService(executorService);
                }
                // 开始处理数据
                Thread thread = Executors.defaultThreadFactory().newThread(redisDelayedQueue::processing);
                delayedQueueThreadList.add(thread);
                // 启动线程
                thread.start();
            }
        }
    }

    private void shutdown() {
        for (Thread thread : delayedQueueThreadList) {
            // 如果线程还存活，需要中断线程
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }
        delayedQueueThreadList.clear();
        delayedQueueThreadList = null;
    }

    private ExecutorService createExecutorService(DelayedQueueProperties.Executor executor) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(executor.getCoreSize(), executor.getMaxSize(), executor.getKeepAliveSecond(),
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(executor.getQueueCapacity()),
                new ThreadFactoryBuilder().setNameFormat(String.format("%s-", "delayedQueue")).build(),
                new ThreadPoolExecutor.AbortPolicy());
        executorService.allowCoreThreadTimeOut(executor.isAllowCoreThreadTimeOut());
        return executorService;
    }
}
