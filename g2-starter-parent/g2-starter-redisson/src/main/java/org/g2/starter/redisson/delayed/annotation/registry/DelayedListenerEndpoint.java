package org.g2.starter.redisson.delayed.annotation.registry;

import org.g2.starter.redisson.delayed.annotation.DelayedListener;
import org.g2.starter.redisson.delayed.annotation.adapter.HandlerAdapter;
import org.g2.starter.redisson.delayed.annotation.adapter.InvocableHandlerMethod;
import org.g2.starter.redisson.delayed.annotation.adapter.MessageListenerAdapter;
import org.g2.starter.redisson.delayed.annotation.listener.MessageListenerContainer;
import org.g2.starter.redisson.delayed.annotation.listener.MessagingMessageListener;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author wuwenxi 2021-12-27
 */
public class DelayedListenerEndpoint {

    private String id;
    private Object bean;
    private Method method;
    private String beanName;
    private DelayedListener delayedListener;
    private BeanFactory beanFactory;

    public DelayedListenerEndpoint() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public DelayedListener getDelayedListener() {
        return delayedListener;
    }

    public void setDelayedListener(DelayedListener delayedListener) {
        this.delayedListener = delayedListener;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 创建消息回调监听
     */
    public void setupListenerContainer(MessageListenerContainer container) {
        Object messageListener = createMessageListener(container);
        messageListener=  new MessageListenerAdapter(((MessagingMessageListener) messageListener));
        container.setupListenerContainer(messageListener);
    }

    private MessagingMessageListener createMessageListener(MessageListenerContainer container) {
        MessagingMessageListener messagingMessageListener = new MessagingMessageListener(this.bean, this.method);
        messagingMessageListener.setHandlerMethod(configureListenerAdapter());
        return messagingMessageListener;
    }

    private HandlerAdapter configureListenerAdapter() {
        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(this.bean, this.method);
        return new HandlerAdapter(invocableHandlerMethod);
    }
}
