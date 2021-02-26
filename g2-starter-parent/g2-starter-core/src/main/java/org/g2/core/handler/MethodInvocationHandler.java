package org.g2.core.handler;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
@FunctionalInterface
public interface MethodInvocationHandler<T> {

    /**
     * 方法执行器
     *
     * @param invocationHandler 责任链调度器
     * @return object
     * @throws Exception 异常信息
     */
    T invoke(InvocationHandler invocationHandler) throws Exception;
}
