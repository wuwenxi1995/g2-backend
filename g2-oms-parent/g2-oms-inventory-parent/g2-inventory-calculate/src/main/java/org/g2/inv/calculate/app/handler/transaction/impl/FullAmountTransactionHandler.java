package org.g2.inv.calculate.app.handler.transaction.impl;

import org.g2.core.CoreConstants;
import org.g2.core.util.StringUtil;
import org.g2.inv.calculate.app.handler.transaction.AbstractTransactionHandler;
import org.g2.inv.calculate.domain.repository.TransactionOperationRepository;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wuwenxi 2022-04-11
 */
@Component
public class FullAmountTransactionHandler extends AbstractTransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(FullAmountTransactionHandler.class);

    private final TransactionOperationRepository transactionOperationRepository;

    public FullAmountTransactionHandler(TransactionOperationRepository transactionOperationRepository,
                                        StockLevelRepository stockLevelRepository) {
        super(stockLevelRepository, transactionOperationRepository);
        this.transactionOperationRepository = transactionOperationRepository;
    }

    @Override
    public String transactionType() {
        return InvCalculateConstants.TransactionType.FULL;
    }

    @Override
    public void handler(String posCode, List<InvTransaction> transactions) {
        Map<String, InvTransaction> transactionMap = filter(transactions);
        if (transactionMap == null) {
            return;
        }
        for (InvTransaction invTransaction : transactionMap.values()) {
            try {
                invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.SUCCESS);
                StockLevel stockLevel = selectOneBySkuAndPos(invTransaction.getSkuCode(), invTransaction.getPosCode());
                if (null == stockLevel) {
                    stockLevel = new StockLevel();
                    stockLevel.setSkuCode(invTransaction.getSkuCode());
                    stockLevel.setPosCode(invTransaction.getPosCode());
                    stockLevel.setInitialAts(invTransaction.getOnHandInv());
                    stockLevel.setOnHand(invTransaction.getOnHandInv());
                    stockLevel.setReserved(invTransaction.getReserveInv());
                    stockLevel.setLastModifiedTime(new Date());
                    stockLevel.setLastFullTime(new Date());
                    transactionOperationRepository.persistence(stockLevel, invTransaction, true, true);
                } else if (stockLevel.getLastFullTime().after(invTransaction.getSourceDate())) {
                    invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.SKIP);
                    transactionOperationRepository.updateOperational(invTransaction, InvTransaction.FILED_PROCESSING_STATUS_CODE);
                } else {
                    // todo:
                    stockLevel.setOnHand(invTransaction.getOnHandInv());
                    stockLevel.setReserved(invTransaction.getReserveInv());
                    stockLevel.setLastFullTime(new Date());
                    stockLevel.setLastModifiedTime(new Date());
                    transactionOperationRepository.persistence(stockLevel, invTransaction, false, true);
                }
            } catch (Exception e) {
                String errMsg = StringUtil.exceptionString(e);
                log.error("处理全量库存事务发生异常,异常信息:{}", errMsg);
                invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.ERROR);
                invTransaction.setErrorMsg(errMsg);
                transactionOperationRepository.updateOperational(invTransaction, InvTransaction.FILED_PROCESSING_STATUS_CODE, InvTransaction.FILED_ERROR_MSG);
            }
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }

}
