package org.g2.core.task;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.lang.NonNull;

import java.util.concurrent.Executors;

/**
 * @author wuwenxi 2022-03-09
 */
public abstract class TaskHandler
        implements InitializingBean, SmartLifecycle, ApplicationListener<ApplicationReadyEvent> {

    private volatile boolean running = false;
    private ApplicationContext applicationContext;

    /**
     * 工作线程
     */
    private Thread worker;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public final void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        this.applicationContext = event.getApplicationContext();
        // 启动任务
        this.worker.start();
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public final void start() {
        synchronized (this) {
            if (isRunning()) {
                return;
            }
            this.running = true;
        }
        doStart();
        this.worker = Executors.defaultThreadFactory().newThread(this::run);
    }

    @Override
    public final void stop() {
        synchronized (this) {
            if (!isRunning()) {
                return;
            }
            this.running = false;
        }
        doStop();
        if (worker.isAlive()) {
            worker.interrupt();
        }
    }

    @Override
    public final boolean isRunning() {
        return running;
    }

    /**
     * 启动任务
     */
    protected abstract void run();

    protected void doStart() {
    }

    protected void doStop() {
    }
}
