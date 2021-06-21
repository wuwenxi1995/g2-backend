package org.g2.starter.lock.infra.exception;

/**
 * @author wuwenxi 2021-06-19
 */
public class LockException extends RuntimeException {

    public LockException(String code, Object... param) {
        super(String.format(code, param), null, true, false);
    }

    public LockException(String code, Throwable cause, Object... param) {
        super(String.format(code, param), cause, true, false);
    }

    public LockException(Throwable cause) {
        super(null, cause, true, false);
    }
}
