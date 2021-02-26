package org.g2.core.handler;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
@FunctionalInterface
public interface InvocationHandler<T> {

    /**
     * 责任链执行器
     *
     * @return object
     * @throws Exception 异常信息
     */
    T proceed() throws Exception;
}
