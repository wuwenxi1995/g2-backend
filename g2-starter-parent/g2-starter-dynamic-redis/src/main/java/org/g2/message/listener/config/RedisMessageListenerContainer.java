package org.g2.message.listener.config;

import org.springframework.context.SmartLifecycle;

/**
 * @author wuwenxi 2022-12-08
 */
public class RedisMessageListenerContainer implements SmartLifecycle {

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
