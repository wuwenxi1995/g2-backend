package org.g2.starter.core.chain.invoker;

/**
 * 调用链执行器
 *
 * @author wuwenxi 2021-07-07
 */
@FunctionalInterface
public interface ChainInvoker {

    /**
     * 带参数带调用链执行器
     *
     * @param param 请求参数
     * @return object
     * @throws Exception 异常信息
     */
    Object proceed(Object... param) throws Exception;
}
