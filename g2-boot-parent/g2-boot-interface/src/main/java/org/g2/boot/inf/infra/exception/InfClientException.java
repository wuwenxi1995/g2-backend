package org.g2.boot.inf.infra.exception;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-17
 */
public class InfClientException extends RuntimeException {

    public InfClientException(String code) {
        super(code, null, true, false);
    }

    public InfClientException(String code, Throwable cause) {
        super(code, cause, true, false);
    }
}
