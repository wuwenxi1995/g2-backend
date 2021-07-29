package org.g2.core.thread.chain;

import org.g2.core.chain.Chain;
import org.g2.core.chain.invoker.base.BaseChainInvoker;
import org.g2.core.helper.ApplicationContextHelper;

import java.util.Collection;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author wuwenxi 2021-07-16
 */
public class ThreadRejectedChainHandler extends BaseChainInvoker {

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
        return ApplicationContextHelper.getApplicationContext().getBeansOfType(ThreadRejectChain.class).values();
    }
}
