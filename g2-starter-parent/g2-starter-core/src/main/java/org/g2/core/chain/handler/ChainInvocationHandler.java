package org.g2.core.chain.handler;

import org.g2.core.chain.Chain;
import org.g2.core.chain.invoker.ChainInvoker;

/**
 * 调用链
 *
 * @author wuwenxi 2021-07-07
 */
@FunctionalInterface
public interface ChainInvocationHandler extends Chain {

    /**
     * 方法执行器
     *
     * @param chainInvoker 责任链调度器
     * @return object
     * @throws Exception 异常信息
     */
    Object invoke(ChainInvoker chainInvoker) throws Exception;
}
