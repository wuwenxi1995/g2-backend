package org.g2.core.handler;

import org.springframework.core.Ordered;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
@FunctionalInterface
public interface MethodInvocationHandler extends Ordered {

    /**
     * 方法执行器
     *
     * @param invocationHandler 责任链调度器
     * @return object
     * @throws Exception 异常信息
     */
    Object invoke(InvocationHandler invocationHandler) throws Exception;

    /**
     * 优先级
     *
     * @return 优先级
     */
    @Override
    default int getOrder() {
        return 0;
    }
}
