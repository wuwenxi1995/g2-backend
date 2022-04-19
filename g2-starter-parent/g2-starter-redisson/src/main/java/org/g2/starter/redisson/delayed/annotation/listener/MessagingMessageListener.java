package org.g2.starter.redisson.delayed.annotation.listener;

import org.g2.starter.redisson.delayed.annotation.adapter.HandlerAdapter;

import java.lang.reflect.Method;

/**
 * @author wuwenxi 2021-12-28
 */
public class MessagingMessageListener {

    private HandlerAdapter handlerAdapter;

    public MessagingMessageListener(Object bean, Method method) {
    }

    public void setHandlerMethod(HandlerAdapter handlerMethod) {
        this.handlerAdapter = handlerMethod;
    }

    public void invoke(Object message) {
        try {
            this.handlerAdapter.invoke(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
