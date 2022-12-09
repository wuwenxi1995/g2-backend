package org.g2.message.handler;

import org.g2.core.helper.FastJsonHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wuwenxi 2022-12-09
 */
public class MessageHandler {

    private Object bean;
    private Method method;
    private Class<?> type;

    public void onMessage(String message) throws InvocationTargetException, IllegalAccessException {
        Object data = FastJsonHelper.stringConvertObject(message, type);
        method.invoke(bean, data);
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Object getBean() {
        return this.bean;
    }
}
