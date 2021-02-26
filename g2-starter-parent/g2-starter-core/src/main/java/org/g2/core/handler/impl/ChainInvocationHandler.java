package org.g2.core.handler.impl;

import org.g2.core.handler.InvocationHandler;
import org.g2.core.handler.MethodInvocationHandler;

import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
public abstract class ChainInvocationHandler<T> implements InvocationHandler<T> {

    private int currentHandlerIndex = -1;
    private List<?> methodInvocationHandlerList;

    protected ChainInvocationHandler(List<? extends MethodInvocationHandler> methodInvocationHandlerList) {
        this.methodInvocationHandlerList = methodInvocationHandlerList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T proceed() throws Exception {
        if (currentHandlerIndex == methodInvocationHandlerList.size() - 1) {
            return this.invoke();
        }
        Object handler = methodInvocationHandlerList.get(++currentHandlerIndex);
        return ((MethodInvocationHandler<T>) handler).invoke(this);
    }

    protected T invoke() {
        return null;
    }
}
