package org.g2.core.util;

/**
 * 公共返回响应结构
 *
 * @author wuwenxi 2022-04-18
 */
public class Result<T> {

    private T content;
    private boolean success;
    private Integer code;
    private String msg;

    public Result(boolean success, Integer code) {
        this.success = success;
        this.code = code;
    }

    public Result(Integer code, String msg) {
        this.success = false;
        this.code = code;
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
