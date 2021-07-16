package org.g2.core.thread.chain;

import org.g2.core.chain.Chain;
import org.g2.core.chain.invoker.base.BaseChainInvoker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author wuwenxi 2021-07-16
 */
public class ThreadRejectedChainHandler extends BaseChainInvoker implements BeanPostProcessor {

    private List<Chain> threadRejectChain = new ArrayList<>();

    @Override
    public RejectedExecutionHandler proceed(Object... param) throws Exception {
        try {
            return (RejectedExecutionHandler) super.proceed(param);
        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }

    @Override
    protected Collection<? extends Chain> initChain() {
        return threadRejectChain;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        if (bean instanceof ThreadRejectChain) {
            threadRejectChain.add((ThreadRejectChain) bean);
        }
        return null;
    }
}
