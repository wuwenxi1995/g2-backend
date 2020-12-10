package org.g2.core.helper;

import org.g2.core.bean.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

/**
 * 执行异步任务
 *
 * @author wenxi.wu@hand-chian.com 2020-12-10
 */
public class AsyncTaskHelper {
    private static final Logger log = LoggerFactory.getLogger(AsyncTaskHelper.class);

    /**
     * 异步操作
     *
     * @param operation 操作内容
     */
    @Async("asyncTaskExecutor")
    public void operation(Operation operation) {
        if (log.isDebugEnabled()) {
            log.debug("async task start ,current thread name :{}", Thread.currentThread().getName());
        }
        operation.operation();
        if (log.isDebugEnabled()) {
            log.debug("async task completion ... ");
        }
    }

    /**
     * 异步操作
     *
     * @param data     操作数据
     * @param consumer 操作内容
     * @param <T>      数据类型
     */
    @Async("asyncTaskExecutor")
    <T> void operation(T data, Consumer<T> consumer) {
        if (log.isDebugEnabled()) {
            log.debug("async task start ,current thread name :{}", Thread.currentThread().getName());
        }
        consumer.accept(data);
        if (log.isDebugEnabled()) {
            log.debug("async task completion ... ");
        }
    }
}
