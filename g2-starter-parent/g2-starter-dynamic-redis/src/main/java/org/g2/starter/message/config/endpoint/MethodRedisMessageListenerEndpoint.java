package org.g2.starter.message.config.endpoint;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author wuwenxi 2022-12-08
 */
public class MethodRedisMessageListenerEndpoint {

    private Object bean;
    private Method method;
    private Class<?> type;
    private int db;
    private String queueName;

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

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodRedisMessageListenerEndpoint that = (MethodRedisMessageListenerEndpoint) o;
        return db == that.db &&
                bean.equals(that.bean) &&
                method.equals(that.method) &&
                queueName.equals(that.queueName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bean, method, db, queueName);
    }
}
