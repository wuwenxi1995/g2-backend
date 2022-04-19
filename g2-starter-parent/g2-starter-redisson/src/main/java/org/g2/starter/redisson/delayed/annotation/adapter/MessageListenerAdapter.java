package org.g2.starter.redisson.delayed.annotation.adapter;

import org.g2.starter.redisson.delayed.annotation.listener.MessagingMessageListener;

/**
 * @author wuwenxi 2021-12-28
 */
public class MessageListenerAdapter {

    private final MessagingMessageListener messagingMessageListener;

    public MessageListenerAdapter(MessagingMessageListener messagingMessageListener) {
        this.messagingMessageListener = messagingMessageListener;
    }

    public void handlerMessage(Object message) {
        this.messagingMessageListener.invoke(message);
    }
}
