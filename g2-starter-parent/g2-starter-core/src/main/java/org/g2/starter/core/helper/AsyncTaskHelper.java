package org.g2.starter.core.helper;

import org.g2.starter.core.util.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

/**
 * 执行异步任务
 * <p>
 * 主线程启动异步线程，如果异步线程操作了主线程中传入的参数，且异步线程在主线程结束前执行完成，
 * 则异步线程修改的内容将影响到主线程中的该对象；否则不会对主线程中对象有影响。此外异步线程中
 * 的异常不会影响到主线程的执行。
 * 因此使用异步线程，最好处理与主线程无关的任务
 * </p>
 *
 * @author wenxi.wu@hand-chian.com 2020-12-10
 */
public class AsyncTaskHelper {
    private static final Logger log = LoggerFactory.getLogger(AsyncTaskHelper.class);

    private static AsyncTaskHelper asyncTaskHelper;

    public AsyncTaskHelper() {
        asyncTaskHelper = this;
    }

    public static void staticOperation(Operation operation) {
        asyncTaskHelper.operation(operation);
    }

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
    @Deprecated
    public <T> void operation(T data, Consumer<T> consumer) {
        if (log.isDebugEnabled()) {
            log.debug("async task start ,current thread name :{}", Thread.currentThread().getName());
        }
        consumer.accept(data);
        if (log.isDebugEnabled()) {
            log.debug("async task completion ... ");
        }
    }
}
