package org.g2.inv.calculate.app.handler.transaction;

import org.g2.core.chain.Chain;
import org.g2.core.chain.invoker.base.BaseChainInvoker;
import org.g2.core.util.StringUtil;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-04-11
 */
@Component
public class TransactionHandlerChain extends BaseChainInvoker {
    private static final Logger log = LoggerFactory.getLogger(TransactionHandlerChain.class);

    private final List<TransactionHandler> transactionHandlers;
    private final Map<String, TransactionHandler> handlerMap;
    private final ReentrantLock lock;

    public TransactionHandlerChain(List<TransactionHandler> transactionHandlers) {
        this.transactionHandlers = transactionHandlers;
        this.handlerMap = transactionHandlers.stream().collect(Collectors.toMap(TransactionHandler::type, Function.identity(), (u, v) -> v, TreeMap::new));
        this.lock = new ReentrantLock(false);
    }

    @Override
    public Object proceed(Object... param) {
        lock.lock();
        try {
            return super.proceed(param);
        } catch (Exception e) {
            log.error("库存事务处理异常, 异常信息:{}", StringUtil.exceptionString(e));
            return invoke();
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