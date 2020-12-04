package org.g2.scheduler.infra.exception;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public class SchedulerException extends RuntimeException {

    private final transient Object[] parameters;

    public SchedulerException(String message) {
        super(message, null, true, false);
        this.parameters = new Object[0];
    }

    public SchedulerException(String message, Object... code) {
        super(String.format(message, code), null, true, false);
        this.parameters = code;
    }

    public SchedulerException(String message, Throwable throwable) {
        super(message, throwable, true, false);
        this.parameters = new Object[0];
    }

    public SchedulerException(String message, Throwable throwable, Object... code) {
        super(String.format(message, code), throwable, true, false);
        this.parameters = code;
    }

    public SchedulerException(Throwable throwable, Object... code) {
        super(null, throwable, true, false);
        this.parameters = code;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

}
