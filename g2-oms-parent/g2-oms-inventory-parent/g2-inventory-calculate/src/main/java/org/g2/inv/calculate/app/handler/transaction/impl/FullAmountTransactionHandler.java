package org.g2.inv.calculate.app.handler.transaction.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.g2.core.CoreConstants;
import org.g2.core.util.DataUniqueUtil;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 全量库存事务同步
 * 1. 筛选相同sku下最后创建的库存事务
 * 2. 判断库存事务创建时间是否小于StockLevel最后一次全量同步时间
 * 3. 更新StockLevel持有量，以及保留量；
 * 需要将原保留量+库存事务保留量更新到现在到到保留量中
 *
 * @author wuwenxi 2022-04-11
 */
@Component
public class FullAmountTransactionHandler extends AbstractTransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(FullAmountTransactionHandler.class);

    private final TransactionOperationRepository transactionOperationRepository;

    public FullAmountTransactionHandler(TransactionOperationRepository transactionOperationRepository,
                                        StockLevelRepository stockLevelRepository,
                                        InvCalculateTriggerService invCalculateTriggerService) {
        super(stockLevelRepository, invCalculateTriggerService);
        this.transactionOperationRepository = transactionOperationRepository;
    }

    @Override
    public String transactionType() {
        return InvCalculateConstants.TransactionType.FULL;
    }

    @Override
    public void handler(String posCode, List<InvTransaction> invTransactions) {
        // 处理相同sku，时间最新的事务
        Map<String, InvTransaction> transactionMap = DataUniqueUtil.filter(invTransactions, new DataUniqueUtil.DataUniqueHandler<String, InvTransaction>() {
            @Override
            public String mapKey(InvTransaction value) {
                return value.getSkuCode();
            }

            @Override
            public InvTransaction choose(List<InvTransaction> operationData) {
                if (operationData.size() > 1) {
                    // 按sourceDate降序排序
                    operationData.sort(Comparator.comparing(InvTransaction::getSourceDate).reversed());
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
        if (transactionMap == null) {
            return;
        }
        List<TransactionTriggerVO> transactionTriggers = new ArrayList<>();
        for (InvTransaction invTransaction : transactionMap.values()) {
            try {
                invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.SUCCESS);
                StockLevel stockLevel = selectOneBySkuAndPos(invTransaction.getSkuCode(), invTransaction.getPosCode());
                boolean isCreate = true;
                if (null == stockLevel) {
                    stockLevel = new StockLevel();
                    stockLevel.setSkuCode(invTransaction.getSkuCode());
                    stockLevel.setPosCode(invTransaction.getPosCode());
                    stockLevel.setInitialAts(invTransaction.getOnHandInv() - invTransaction.getReserveInv());
                    stockLevel.setOnHand(invTransaction.getOnHandInv());
                    stockLevel.setReserved(invTransaction.getReserveInv());
                    stockLevel.setLastModifiedTime(new Date());
                    stockLevel.setLastFullTime(new Date());
                } else if (stockLevel.getLastFullTime().after(invTransaction.getSourceDate())) {
                    invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.SKIP);
                    transactionOperationRepository.updateOperational(invTransaction, InvTransaction.FILED_PROCESSING_STATUS_CODE);
                    continue;
                } else {
                    stockLevel.setOnHand(invTransaction.getOnHandInv());
                    // 确保已有的保留量准确
                    stockLevel.setReserved(defValue(stockLevel.getReserved()) + invTransaction.getReserveInv());
                    stockLevel.setLastFullTime(new Date());
                    stockLevel.setLastModifiedTime(new Date());
                    isCreate = false;
                }
                transactionOperationRepository.persistence(stockLevel, invTransaction, isCreate);
                transactionTriggers.add(preparedTriggerData(invTransaction));
            } catch (Exception e) {
                String errMsg = StringUtil.exceptionString(e);
                log.error("处理全量库存事务发生异常,skuCode: {},posCode:{}, 异常信息:{}", invTransaction.getSkuCode(), posCode, errMsg);
                invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.ERROR);
                invTransaction.setErrorMsg(errMsg);
                transactionOperationRepository.updateOperational(invTransaction, InvTransaction.FILED_PROCESSING_STATUS_CODE, InvTransaction.FILED_ERROR_MSG);
            }
        }
        this.trigger(transactionTriggers);
    }

    @Override
    public int getOrder() {
        return 2;
    }

}
