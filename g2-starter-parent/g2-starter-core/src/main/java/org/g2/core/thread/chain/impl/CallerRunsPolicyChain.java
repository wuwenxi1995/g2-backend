package org.g2.core.thread.chain.impl;

import org.g2.core.chain.invoker.ChainInvoker;
import org.g2.core.thread.ThreadRejected;
import org.g2.core.thread.chain.ThreadRejectChain;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wuwenxi 2021-07-16
 */
@Component
public class CallerRunsPolicyChain implements ThreadRejectChain {

    @Override
    public Object invoke(ChainInvoker chainInvoker, Object... param) throws Exception {
        String rejected = (String) param[0];
        if (!ThreadRejected.CallerRunsPolicy.getValue().equals(rejected)) {
            return chainInvoker.proceed(param);
        }
        return new ThreadPoolExecutor.CallerRunsPolicy();
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
