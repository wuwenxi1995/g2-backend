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
    private final TransactionOperationRepository transactionOperationRepository;

    protected AbstractTransactionHandler(StockLevelRepository stockLevelRepository, TransactionOperationRepository transactionOperationRepository) {
        this.stockLevelRepository = stockLevelRepository;
        this.transactionOperationRepository = transactionOperationRepository;
    }

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

    protected Map<String, InvTransaction> filter(List<InvTransaction> invTransactions) {
        // 处理相同sku，时间最新的事务
        return DataUniqueUtil.filter(invTransactions, new DataUniqueUtil.DataUniqueHandler<String, InvTransaction>() {
            @Override
            public String mapKey(InvTransaction value) {
                return value.getSkuCode();
            }

            @Override
            public InvTransaction choose(List<InvTransaction> operationData) {
                if (operationData.size() > 1) {
                    operationData.sort(Comparator.comparing(InvTransaction::getSourceDate));
                }
                return operationData.get(0);
            }

            @Override
            public void abandonData(List<InvTransaction> abandonVar) {
                if (CollectionUtils.isEmpty(abandonVar)) {
                    return;
                }
                for (InvTransaction invTransaction : abandonVar) {
                    invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.SKIP);
                }
                transactionOperationRepository.updateOperational(abandonVar, InvTransaction.FILED_PROCESSING_STATUS_CODE);
            }

            @Override
            public boolean parallel() {
                return false;
            }
        });
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
