package org.g2.core.chain;

/**
 * 调用链执行器
 *
 * @author wuwenxi 2021-07-07
 */
@FunctionalInterface
public interface ChainHandler {

    /**
     * 调度责任链
     *
     * @return object
     * @throws Exception 异常信息
     */
    Object proceed() throws Exception;

    /**
     * 带参数带调用链执行器
     *
     * @param param 请求参数
     * @return object
     * @throws Exception 异常信息
     */
    default Object proceed(Object... param) throws Exception {
        return null;
    }
}
