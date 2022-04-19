package org.g2.inv.api.dto;

import lombok.Data;

/**
 * @author wuwenxi 2022-04-15
 */
@Data
public class ApiIdempotentRequest<T> {

    /**
     * 请求内容
     */
    private T content;
    /**
     * token
     */
    private String token;
}
