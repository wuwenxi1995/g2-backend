package org.g2.starter.core.chain.handler;

import org.g2.starter.core.chain.Chain;
import org.g2.starter.core.chain.invoker.ChainInvoker;

/**
 * @author wuwenxi 2021-07-07
 */
@FunctionalInterface
public interface ParamChainInvocationHandler extends Chain {

    /**
     * 带参数带调用链
     *
     * @param chainInvoker 责任链调度器
     * @param param        请求参数
     * @return object
     * @throws Exception 异常信息
     */
    Object invoke(ChainInvoker chainInvoker, Object... param) throws Exception;
}
