package org.g2.scheduler.infra.exception;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public class SchedulerException extends RuntimeException {

    public SchedulerException(String message) {
        super(message);
    }

    public SchedulerException(String message, Object... code) {
        super(String.format(message, code));
    }

    public SchedulerException(String message, Throwable throwable) {
        super(String.format(message, throwable.getMessage()));
    }
}
