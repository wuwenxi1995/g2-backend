package org.g2.core.handler.impl;

import org.g2.core.handler.InvocationHandler;
import org.g2.core.handler.MethodInvocationHandler;
import org.g2.core.helper.ApplicationContextHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-26
 */
public abstract class ChainInvocationHandler implements InvocationHandler {

    private int currentHandlerIndex = -1;
    private final List<? extends MethodInvocationHandler> methodInvocationHandlerList;

    protected ChainInvocationHandler() {
        // 初始化调用链
        Collection<? extends MethodInvocationHandler> values = ApplicationContextHelper.getApplicationContext().getBeansOfType(beanType()).values();
        methodInvocationHandlerList = new ArrayList<>(values);
        // 调用链排序
        methodInvocationHandlerList.sort(Comparator.comparing(MethodInvocationHandler::getOrder));
    }

    @Override
    public Object proceed() throws Exception {
        if (currentHandlerIndex == methodInvocationHandlerList.size() - 1) {
            return this.invoke();
        }
        MethodInvocationHandler handler = methodInvocationHandlerList.get(++currentHandlerIndex);
        return handler.invoke(this);
    }

    protected Object invoke() {
        return null;
    }

    /**
     * 返回调用链类型
     *
     * @return class
     */
    protected abstract Class<? extends MethodInvocationHandler> beanType();
}
