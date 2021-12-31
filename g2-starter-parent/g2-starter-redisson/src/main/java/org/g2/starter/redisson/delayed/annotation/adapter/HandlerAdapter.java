package org.g2.starter.redisson.delayed.annotation.adapter;

/**
 * @author wuwenxi 2021-12-28
 */
public class HandlerAdapter {

    private final InvocableHandlerMethod invokerHandlerMethod;

    public HandlerAdapter(InvocableHandlerMethod invocableHandlerMethod) {
        this.invokerHandlerMethod = invocableHandlerMethod;
    }

    public Object invoke(Object message) throws Exception {
        return invokerHandlerMethod.invokeMethod(message);
    }
}
