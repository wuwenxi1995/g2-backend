package org.g2.inv.calculate.app.handler.transaction.impl;

import org.g2.core.CoreConstants;
import org.g2.core.util.StringUtil;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.inv.calculate.app.handler.transaction.AbstractTransactionHandler;
import org.g2.inv.calculate.domain.repository.TransactionOperationRepository;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-04-11
 */
@Component
public class IncrementAmountTransactionHandler extends AbstractTransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(IncrementAmountTransactionHandler.class);

    private final TransactionOperationRepository transactionOperationRepository;

    public IncrementAmountTransactionHandler(StockLevelRepository stockLevelRepository,
                                             TransactionOperationRepository transactionOperationRepository,
                                             InvCalculateTriggerService invCalculateTriggerService) {
        super(stockLevelRepository, invCalculateTriggerService);
        this.transactionOperationRepository = transactionOperationRepository;
    }

    @Override
    public String transactionType() {
        return InvCalculateConstants.TransactionType.INCREMENT;
    }

    @Override
    public void handler(String posCode, List<InvTransaction> transactions) {
        Map<String, List<InvTransaction>> groupMap = transactions.stream()
                .peek(e -> e.setProcessingStatusCode(CoreConstants.ProcessStatus.SUCCESS))
                .collect(Collectors.groupingBy(InvTransaction::getSkuCode));
        List<TransactionTriggerVO> transactionTriggers = new ArrayList<>();
        for (Map.Entry<String, List<InvTransaction>> entry : groupMap.entrySet()) {
            String key = entry.getKey();
            List<InvTransaction> value = entry.getValue();
            try {
                StockLevel stockLevel = selectOneBySkuAndPos(key, posCode);
                boolean isCreate;
                if (isCreate = (stockLevel == null)) {
                    long onHandInc = value.stream().mapToLong(InvTransaction::getOnHandInv).sum();
                    long reserveInc = value.stream().mapToLong(InvTransaction::getReserveInv).sum();
                    stockLevel = new StockLevel();
                    stockLevel.setSkuCode(key);
                    stockLevel.setPosCode(posCode);
                    stockLevel.setInitialAts(onHandInc - reserveInc);
                    stockLevel.setOnHand(onHandInc);
                    stockLevel.setReserved(reserveInc);
                    stockLevel.setLastModifiedTime(new Date());
                } else {
                    StockLevel finalStockLevel = stockLevel;
                    List<InvTransaction> filterTs = value.stream().filter(e -> {
                        // 如果增量库存事务时间在最新一次全量库存事务之前，跳过
                        boolean flag = finalStockLevel.getLastFullTime().after(e.getSourceDate());
                        if (!flag) {
                            e.setProcessingStatusCode(CoreConstants.ProcessStatus.SKIP);
                        }
                        return flag;
                    }).collect(Collectors.toList());
                    long onHandInc = filterTs.stream().mapToLong(InvTransaction::getOnHandInv).sum();
                    long reserveInc = filterTs.stream().mapToLong(InvTransaction::getReserveInv).sum();
                    stockLevel.setOnHand(defValue(stockLevel.getOnHand()) + onHandInc);
                    stockLevel.setReserved(defValue(stockLevel.getReserved()) + reserveInc);
                    stockLevel.setLastModifiedTime(new Date());
                }
                transactionOperationRepository.persistence(stockLevel, value, isCreate);
                transactionTriggers.add(preparedTriggerData(value.get(0)));
            } catch (Exception e) {
                String errMsg = StringUtil.exceptionString(e);
                log.error("处理增量存事务发生异常,skuCode: {},posCode:{}, 异常信息:{}", key, posCode, errMsg);
                for (InvTransaction invTransaction : value) {
                    invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.ERROR);
                    invTransaction.setErrorMsg(errMsg);
                }
                transactionOperationRepository.updateOperational(value, InvTransaction.FILED_PROCESSING_STATUS_CODE, InvTransaction.FILED_ERROR_MSG);
            }
        }
        this.trigger(transactionTriggers);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
