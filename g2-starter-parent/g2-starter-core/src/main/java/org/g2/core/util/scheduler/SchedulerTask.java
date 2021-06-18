package org.g2.core.util.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-11
 */
public class SchedulerTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);

    private ScheduledThreadPoolExecutor scheduled;
    private ThreadPoolExecutor executor;
    private RunTask runTask;

    public SchedulerTask(ScheduledThreadPoolExecutor scheduled, ThreadPoolExecutor executor) {
        this.executor = executor;
        this.scheduled = scheduled;
    }

    public SchedulerTask(ScheduledThreadPoolExecutor scheduled, ThreadPoolExecutor executor, RunTask runTask) {
        this(scheduled, executor);
        this.runTask = runTask;
    }

    public void setScheduled(ScheduledThreadPoolExecutor scheduled) {
        this.scheduled = scheduled;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public void setRunTask(RunTask runTask) {
        this.runTask = runTask;
    }

    @Override
    public void run() {
        Assert.notNull(scheduled, "request a scheduler executor");
        Assert.notNull(executor, "request a thread executor");
        Assert.notNull(runTask, "request a task");
        try {
            executor.submit(runTask);
        } catch (Exception e) {
            logger.error("");
        } finally {
            if (!scheduled.isShutdown()) {
                scheduled.schedule(this, runTask.getDelay(), runTask.getTimeUnit());
            }
        }
    }
}
