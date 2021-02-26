package org.g2.core.handler;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
@FunctionalInterface
public interface InvocationHandler {

    /**
     * 责任链执行器
     *
     * @return object
     * @throws Exception 异常信息
     */
    Object proceed() throws Exception;
}
