package org.g2.core.handler.impl;

import org.g2.core.handler.InvocationHandler;
import org.g2.core.handler.MethodInvocationHandler;

import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
public abstract class ChainInvocationHandler implements InvocationHandler {

    private int currentHandlerIndex = -1;
    private List<?> methodInvocationHandlerList;

    protected ChainInvocationHandler(List<? extends MethodInvocationHandler> methodInvocationHandlerList) {
        this.methodInvocationHandlerList = methodInvocationHandlerList;
    }

    @Override
    public Object proceed() throws Exception {
        if (currentHandlerIndex == methodInvocationHandlerList.size() - 1) {
            return this.invoke();
        }
        Object handler = methodInvocationHandlerList.get(++currentHandlerIndex);
        return ((MethodInvocationHandler) handler).invoke(this);
    }

    protected Object invoke() {
        return null;
    }
}
