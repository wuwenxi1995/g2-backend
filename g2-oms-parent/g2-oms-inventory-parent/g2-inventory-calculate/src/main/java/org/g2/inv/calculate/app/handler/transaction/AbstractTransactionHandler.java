package org.g2.inv.calculate.app.handler.transaction;

import org.g2.core.chain.invoker.ChainInvoker;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author wuwenxi 2022-04-11
 */
public abstract class AbstractTransactionHandler implements TransactionHandler {

    @Autowired
    private InvCalculateTriggerService invCalculateTriggerService;

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(ChainInvoker chainInvoker, Object... param) throws Exception {
        Map<InvTransaction, List<InvTransaction>> transactionListMap = (Map<InvTransaction, List<InvTransaction>>) param[0];
        transactionListMap.forEach((key, value) -> {
            if (transactionType().equals(key.getTransactionType())) {
                handler(key.getPosCode(), value);
            }
        });
        return chainInvoker.proceed(param);
    }

    protected void trigger(List<TransactionTriggerVO> triggers) {
        invCalculateTriggerService.triggerByTransaction(triggers);
    }
}
