package org.g2.inv.api.dto;

import lombok.Data;
import org.g2.inv.infra.InvApiConstants;

/**
 * 统一响应状态
 *
 * @author wuwenxi 2022-04-15
 */
@Data
public class ApiResponse<T> {

    private T content;
    private boolean success;
    private Integer code;
    private String msg;

    public static <A> ApiResponse<A> ok() {
        return ok(null);
    }

    public static <A> ApiResponse<A> ok(A content) {
        ApiResponse<A> response = new ApiResponse<>();
        response.setContent(content);
        response.setSuccess(true);
        response.setCode(InvApiConstants.ApiResponseCode.SUCCESS.getCode());
        return response;
    }

    public static <A> ApiResponse<A> error(String msg, InvApiConstants.ResponseInfo responseCode) {
        ApiResponse<A> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(responseCode.getCode());
        response.setMsg(responseCode.getMsg());
        if (msg != null) {
            response.setMsg(msg);
        }
        return response;
    }
}
