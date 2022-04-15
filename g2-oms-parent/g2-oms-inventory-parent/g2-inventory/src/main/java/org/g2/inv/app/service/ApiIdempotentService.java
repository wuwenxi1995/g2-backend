package org.g2.inv.app.service;

import org.g2.inv.api.dto.ApiResponse;

import java.util.function.Supplier;

/**
 * @author wuwenxi 2022-04-15
 */
public interface ApiIdempotentService {

    /**
     * 生成token
     *
     * @return token
     */
    String token();

    /**
     * token校验
     *
     * @param token    token
     * @param supplier 校验后续操作
     * @param <T>      返回内容
     * @return ApiResponse
     */
    <T> ApiResponse<T> idempotentOperation(String token, Supplier<T> supplier);
}
