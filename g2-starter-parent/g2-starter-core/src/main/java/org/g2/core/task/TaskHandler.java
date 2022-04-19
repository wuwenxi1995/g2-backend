package org.g2.core.task;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.lang.NonNull;

/**
 * @author wuwenxi 2022-03-09
 */
public abstract class TaskHandler
        implements InitializingBean, SmartLifecycle, ApplicationListener<ApplicationReadyEvent> {

    private volatile boolean running = false;
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public final void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        this.applicationContext = event.getApplicationContext();
        // 启动任务
        run();
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
