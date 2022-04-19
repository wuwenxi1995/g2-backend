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
public class DefaultTransactionHandler extends AbstractTransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultTransactionHandler.class);

    private final TransactionOperationRepository transactionOperationRepository;

    public DefaultTransactionHandler(StockLevelRepository stockLevelRepository,
                                     TransactionOperationRepository transactionOperationRepository,
                                     InvCalculateTriggerService invCalculateTriggerService) {
        super(stockLevelRepository, invCalculateTriggerService);
        this.transactionOperationRepository = transactionOperationRepository;
    }

    @Override
    public String transactionType() {
        return InvCalculateConstants.TransactionType.DEFAULT_TYPE;
    }

    @Override
    public void handler(String posCode, List<InvTransaction> transactions) {
        Map<String, List<InvTransaction>> groupMap = transactions.stream()
                .peek(e -> e.setProcessingStatusCode(CoreConstants.ProcessStatus.SUCCESS))
                .collect(Collectors.groupingBy(InvTransaction::getSkuCode));
        List<TransactionTriggerVO> transactionTriggers = new ArrayList<>();
        groupMap.forEach((key, value) -> {
            try {
                StockLevel stockLevel = selectOneBySkuAndPos(key, posCode);
                long onHandInc = value.stream().mapToLong(InvTransaction::getOnHandInv).sum();
                long reserveInc = value.stream().mapToLong(InvTransaction::getReserveInv).sum();
                boolean isCreate;
                if (isCreate = (stockLevel == null)) {
                    stockLevel = new StockLevel();
                    stockLevel.setPosCode(posCode);
                    stockLevel.setSkuCode(key);
                    stockLevel.setInitialAts(onHandInc - reserveInc);
                    stockLevel.setOnHand(onHandInc);
                    stockLevel.setReserved(reserveInc);
                    stockLevel.setLastModifiedTime(new Date());
                } else {
                    stockLevel.setOnHand(defValue(stockLevel.getOnHand()) + onHandInc);
                    stockLevel.setReserved(defValue(stockLevel.getReserved()) + reserveInc);
                    stockLevel.setLastModifiedTime(new Date());
                }
                transactionOperationRepository.persistence(stockLevel, value, isCreate);
                transactionTriggers.add(preparedTriggerData(value.get(0)));
            } catch (Exception e) {
                String errMsg = StringUtil.exceptionString(e);
                log.error("处理普通库存事务发生异常,skuCode: {},posCode:{}, 异常信息:{}", key, posCode, errMsg);
                for (InvTransaction invTransaction : value) {
                    invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.ERROR);
                    invTransaction.setErrorMsg(errMsg);
                }
                transactionOperationRepository.updateOperational(value, InvTransaction.FILED_PROCESSING_STATUS_CODE, InvTransaction.FILED_ERROR_MSG);
            }
        });
        this.trigger(transactionTriggers);
    }
}
