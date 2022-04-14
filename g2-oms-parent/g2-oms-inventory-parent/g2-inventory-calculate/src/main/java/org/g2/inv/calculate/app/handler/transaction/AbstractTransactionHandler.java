package org.g2.inv.calculate.app.handler.transaction;

import org.g2.core.chain.invoker.ChainInvoker;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;

import java.util.List;
import java.util.Map;

/**
 * @author wuwenxi 2022-04-11
 */
public abstract class AbstractTransactionHandler implements TransactionHandler {

    private final StockLevelRepository stockLevelRepository;
    private final InvCalculateTriggerService invCalculateTriggerService;

    protected AbstractTransactionHandler(StockLevelRepository stockLevelRepository, InvCalculateTriggerService invCalculateTriggerService) {
        this.stockLevelRepository = stockLevelRepository;
        this.invCalculateTriggerService = invCalculateTriggerService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(ChainInvoker chainInvoker, Object... param) throws Exception {
        Map<String, Map<String, List<InvTransaction>>> transactionListMap = (Map<String, Map<String, List<InvTransaction>>>) param[0];
        // 默认库存事务
        if (transactionType().equals(InvCalculateConstants.TransactionType.DEFAULT_TYPE)) {
            transactionListMap.values().forEach(e -> e.forEach(this::handler));
        }
        // 增量或全量库存事务
        else if (transactionListMap.containsKey(transactionType())) {
            transactionListMap.get(transactionType()).forEach(this::handler);
            transactionListMap.remove(transactionType());
        }
        return chainInvoker.proceed(param);
    }

    protected Long defValue(Long value) {
        return value != null ? value : 0;
    }

    protected StockLevel selectOneBySkuAndPos(String skuCode, String posCode) {
        StockLevel stockLevel = new StockLevel();
        stockLevel.setSkuCode(skuCode);
        stockLevel.setPosCode(posCode);
        return stockLevelRepository.selectOne(stockLevel);
    }

    protected TransactionTriggerVO preparedTriggerData(InvTransaction invTransaction) {
        TransactionTriggerVO transactionTrigger = new TransactionTriggerVO();
        transactionTrigger.setMasterSkuCode(invTransaction.getSkuCode());
        transactionTrigger.setPosCode(invTransaction.getPosCode());
        return transactionTrigger;
    }

    protected void trigger(List<TransactionTriggerVO> triggers) {
        // 触发服务点库存计算
        invCalculateTriggerService.triggerByTransaction(triggers);
    }
}
