package org.g2.inv.calculate.app.handler.transaction;

import org.apache.commons.collections4.CollectionUtils;
import org.g2.core.CoreConstants;
import org.g2.core.chain.invoker.ChainInvoker;
import org.g2.core.util.DataUniqueUtil;
import org.g2.inv.calculate.domain.repository.TransactionOperationRepository;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author wuwenxi 2022-04-11
 */
public abstract class AbstractTransactionHandler implements TransactionHandler {

    private final StockLevelRepository stockLevelRepository;

    protected AbstractTransactionHandler(StockLevelRepository stockLevelRepository) {
        this.stockLevelRepository = stockLevelRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(ChainInvoker chainInvoker, Object... param) throws Exception {
        Map<String, Map<String, List<InvTransaction>>> transactionListMap = (Map<String, Map<String, List<InvTransaction>>>) param[0];
        if (transactionListMap.containsKey(transactionType())) {
            transactionListMap.get(transactionType()).forEach(this::handler);
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
    }
}
