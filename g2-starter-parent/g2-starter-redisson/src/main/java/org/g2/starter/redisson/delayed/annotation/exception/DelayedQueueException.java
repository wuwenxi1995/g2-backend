package org.g2.starter.redisson.delayed.annotation.exception;

/**
 * @author wuwenxi 2021-12-31
 */
public class DelayedQueueException extends RuntimeException {

    public DelayedQueueException() {
    }

    public DelayedQueueException(String format, Object... args) {
        super(String.format(format, args), null, true, true);
    }

    public DelayedQueueException(String format, Throwable throwable, Object... args) {
        super(String.format(format, args), throwable, true, true);
    }
}
