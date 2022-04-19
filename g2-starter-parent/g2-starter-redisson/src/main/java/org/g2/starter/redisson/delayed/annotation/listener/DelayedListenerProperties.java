package org.g2.starter.redisson.delayed.annotation.listener;

import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2021-12-28
 */
public class DelayedListenerProperties {

    private String delayedQueue;
    private int delayed;
    private TimeUnit timeUnit;
    private Executor executor;
    private Object target;
    private Method method;
    private String targetName;
    private String endpointId;
    private BeanFactory beanFactory;

    public String getDelayedQueue() {
        return delayedQueue;
    }

    public void setDelayedQueue(String delayedQueue) {
        this.delayedQueue = delayedQueue;
    }

    public int getDelayed() {
        return delayed;
    }

    public void setDelayed(int delayed) {
        this.delayed = delayed;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
