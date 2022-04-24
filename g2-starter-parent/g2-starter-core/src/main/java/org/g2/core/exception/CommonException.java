package org.g2.core.exception;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
public class CommonException extends RuntimeException {

    private String code;
    private final transient Object[] parameters;

    public CommonException(String code, Object... param) {
        super(code, null, true, true);
        this.code = code;
        this.parameters = param;
    }

    public CommonException(String code, Throwable cause, Object... param) {
        super(code, cause, true, true);
        this.code = code;
        this.parameters = param;
    }

    public CommonException(String code, Throwable cause) {
        super(code, cause, true, true);
        this.code = code;
        this.parameters = new Object[]{0};
    }

    public CommonException(Throwable cause, Object... param) {
        super(null, cause, true, true);
        this.parameters = param;
    }


    public Object[] getParameters() {
        return this.parameters;
    }

    public String getCode() {
        return this.code;
    }
}
