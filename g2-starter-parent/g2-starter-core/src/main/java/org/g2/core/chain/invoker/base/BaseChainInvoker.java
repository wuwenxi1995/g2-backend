package org.g2.core.chain.invoker.base;

import org.g2.core.chain.Chain;
import org.g2.core.chain.handler.ChainInvocationHandler;
import org.g2.core.chain.handler.ParamChainInvocationHandler;
import org.g2.core.chain.invoker.ChainInvoker;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author wuwenxi 2021-07-07
 */
public abstract class BaseChainInvoker implements ChainInvoker {

    private int currentHandlerIndex = -1;
    private final List<? extends Chain> chainInvocationHandlerList;

    protected BaseChainInvoker() {
        // 初始化调用链
        Collection<? extends Chain> chainInvocationHandlers = initChain();
        Assert.notEmpty(chainInvocationHandlers, "require init chain handler");
        chainInvocationHandlerList = new ArrayList<>(chainInvocationHandlers);
        // 调用链排序
        if (chainInvocationHandlerList.size() > 1) {
            chainInvocationHandlerList.sort(Comparator.comparing(Chain::getOrder));
        }
    }

    @Override
    public Object proceed(Object... param) throws Exception {
        if (currentHandlerIndex == chainInvocationHandlerList.size() - 1) {
            return invoke();
        }
        Chain handler = chainInvocationHandlerList.get(++currentHandlerIndex);
        if (handler instanceof ChainInvocationHandler) {
            return ((ChainInvocationHandler) handler).invoke(this);
        } else if (handler instanceof ParamChainInvocationHandler) {
            return ((ParamChainInvocationHandler) handler).invoke(this, param);
        }
        return invoke();
    }

    protected Object invoke() {
        return null;
    }

    /**
     * 返回调用链集合
     *
     * @return 调用链集合
     */
    protected abstract Collection<? extends Chain> initChain();
}
