package org.g2.inv.calculate.app.handler.transaction;

import org.g2.core.chain.invoker.ChainInvoker;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wuwenxi 2022-04-11
 */
public abstract class AbstractTransactionHandler implements TransactionHandler {

    @Autowired
    private InvCalculateTriggerService invCalculateTriggerService;

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(ChainInvoker chainInvoker, Object... param) throws Exception {
        if (!transactionType().equals(param[0])) {
            return chainInvoker.proceed(param);
        }
        return handler((String) param[1], (List<InvTransaction>) param[2]);
    }

    protected void trigger(List<TransactionTriggerVO> triggers) {
        invCalculateTriggerService.triggerByTransaction(triggers);
    }

    /**
     * 事务库存处理
     *
     * @param posCode      门店编码
     * @param transactions 库存事务
     * @return 处理结果
     */
    protected abstract Object handler(String posCode, List<InvTransaction> transactions);
}
