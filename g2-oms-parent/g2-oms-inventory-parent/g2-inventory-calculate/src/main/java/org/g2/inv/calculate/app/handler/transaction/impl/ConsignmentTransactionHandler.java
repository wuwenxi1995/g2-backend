package org.g2.inv.calculate.app.handler.transaction.impl;

import org.g2.core.CoreConstants;
import org.g2.core.util.StringUtil;
import org.g2.inv.calculate.app.handler.transaction.AbstractTransactionHandler;
import org.g2.inv.calculate.domain.repository.TransactionOperationRepository;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wuwenxi 2022-04-11
 */
@Component
public class ConsignmentTransactionHandler extends AbstractTransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(ConsignmentTransactionHandler.class);

    private final InvTransactionRepository invTransactionRepository;
    private final TransactionOperationRepository transactionOperationRepository;

    public ConsignmentTransactionHandler(StockLevelRepository stockLevelRepository,
                                         InvTransactionRepository invTransactionRepository,
                                         TransactionOperationRepository transactionOperationRepository) {
        super(stockLevelRepository, transactionOperationRepository);
        this.invTransactionRepository = invTransactionRepository;
        this.transactionOperationRepository = transactionOperationRepository;
    }

    @Override
    public String transactionType() {
        return InvCalculateConstants.TransactionType.CONSIGNMENT;
    }

    @Override
    public void handler(String posCode, List<InvTransaction> transactions) {
        List<TransactionTriggerVO> triggers = new ArrayList<>();
        for (InvTransaction transaction : transactions) {
            try {
                BigDecimal onHandInc = new BigDecimal(defValue(transaction.getOnHandInv()).toString());
                BigDecimal reserveInc = new BigDecimal(defValue(transaction.getReserveInv()).toString());
                transaction.setProcessingStatusCode(CoreConstants.ProcessStatus.SUCCESS);
                StockLevel stockLevel = selectOneBySkuAndPos(transaction.getSkuCode(), transaction.getPosCode());
                // todo: 待确定是否需要更新缓存
                if (null == stockLevel) {
                    stockLevel = new StockLevel();
                    stockLevel.setSkuCode(transaction.getSkuCode());
                    stockLevel.setPosCode(transaction.getPosCode());
                    stockLevel.setInitialAts(onHandInc.subtract(reserveInc).longValue());
                    stockLevel.setOnHand(onHandInc.longValue());
                    stockLevel.setReserved(reserveInc.longValue());
                    stockLevel.setLastModifiedTime(new Date());
                    this.transactionOperationRepository.persistence(stockLevel, transaction, true, true);
                } else {
                    BigDecimal onHand = new BigDecimal(defValue(stockLevel.getOnHand())).add(onHandInc);
                    BigDecimal reserve = new BigDecimal(defValue(stockLevel.getReserved())).add(reserveInc);
                    stockLevel.setOnHand(onHand.longValue());
                    stockLevel.setReserved(reserve.longValue());
                    stockLevel.setLastModifiedTime(new Date());
                    this.transactionOperationRepository.persistence(stockLevel, transaction, false, false);
                }
                triggers.add(preparedTriggerData(transaction));
            } catch (Exception e) {
                String errMsg = StringUtil.exceptionString(e);
                log.error("处理配货单库存事务发生异常,异常信息:{}", errMsg);
                transaction.setProcessingStatusCode(CoreConstants.ProcessStatus.ERROR);
                transaction.setErrorMsg(errMsg);
                this.invTransactionRepository.updateOptional(transaction, InvTransaction.FILED_PROCESSING_STATUS_CODE, InvTransaction.FILED_ERROR_MSG);
            }
        }
        //
        this.trigger(triggers);
    }
}
