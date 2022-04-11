package org.g2.inv.calculate.app.handler.transaction;

import org.g2.core.chain.Chain;
import org.g2.core.chain.invoker.base.BaseChainInvoker;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-04-11
 */
@Component
public class TransactionHandlerChain extends BaseChainInvoker {

    private final List<TransactionHandler> transactionHandlers;
    private final Map<String, TransactionHandler> handlerMap;
    private final ReentrantLock lock;

    public TransactionHandlerChain(List<TransactionHandler> transactionHandlers) {
        this.transactionHandlers = transactionHandlers;
        this.handlerMap = transactionHandlers.stream().collect(Collectors.toMap(TransactionHandler::transactionType, Function.identity()));
        this.lock = new ReentrantLock(false);
    }

    @Override
    public Object proceed(Object... param) throws Exception {
        lock.lock();
        try {
            return super.proceed(param);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected Object invoke() {
        // 重新初始化
        restCurrentHandlerIndex();
        return null;
    }

    @Override
    protected Collection<? extends Chain> initChain() {
        return transactionHandlers;
    }

    public TransactionHandler getTransactionHandler(String transactionType) {
        return handlerMap.getOrDefault(transactionType, handlerMap.get(InvCalculateConstants.TransactionType.DEFAULT_TYPE));
    }

}
