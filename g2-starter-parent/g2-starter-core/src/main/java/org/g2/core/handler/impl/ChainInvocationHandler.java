package org.g2.core.handler.impl;

import org.g2.core.exception.CommonException;
import org.g2.core.handler.InvocationHandler;
import org.g2.core.handler.MethodInvocationHandler;

import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
public abstract class ChainInvocationHandler implements InvocationHandler {

    private int currentHandlerIndex = -1;
    protected List<?> methodInvocationHandlerList;

    protected ChainInvocationHandler() {
    }

    protected ChainInvocationHandler(List<? extends MethodInvocationHandler> methodInvocationHandlerList) {
        this.methodInvocationHandlerList = methodInvocationHandlerList;
    }

    protected void setMethodInvocationHandlerList() {
        this.methodInvocationHandlerList = null;
    }

    @Override
    public Object proceed() throws Exception {
        if (methodInvocationHandlerList == null || methodInvocationHandlerList.size() < 1) {
            throw new CommonException("MethodInvocationHandler is null");
        }
        if (currentHandlerIndex == methodInvocationHandlerList.size() - 1) {
            throw new CommonException("No suitable handler found");
        }
        Object handler = methodInvocationHandlerList.get(++currentHandlerIndex);
        return ((MethodInvocationHandler) handler).invoke(this);
    }
}
