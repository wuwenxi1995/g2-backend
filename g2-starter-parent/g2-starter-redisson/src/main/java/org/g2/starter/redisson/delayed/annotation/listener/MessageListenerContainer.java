package org.g2.starter.redisson.delayed.annotation.listener;

import org.springframework.context.SmartLifecycle;

/**
 * @author wuwenxi 2021-12-27
 */
public interface MessageListenerContainer extends SmartLifecycle {

    /**
     * 消息回到监听器
     *
     * @param messageListener 消息监听器
     */
    void setupListenerContainer(Object messageListener);
}
