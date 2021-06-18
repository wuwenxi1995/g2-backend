package org.g2.core.util.scheduler;

import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2021-06-04
 */
public abstract class RunTask implements Runnable {

    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private Integer delay = 60;

    TimeUnit getTimeUnit() {
        return timeUnit;
    }

    Integer getDelay() {
        return delay;
    }

    public RunTask() {
    }

    protected RunTask(TimeUnit timeUnit, Integer delay) {
        this.timeUnit = timeUnit;
        this.delay = delay;
    }

    @Override
    public abstract void run();
}
