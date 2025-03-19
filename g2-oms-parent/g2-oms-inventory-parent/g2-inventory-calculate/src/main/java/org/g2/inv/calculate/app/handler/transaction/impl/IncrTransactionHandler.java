package org.g2.inv.calculate.app.handler.transaction.impl;

import org.g2.starter.core.CoreConstants;
import org.g2.starter.core.util.StringUtil;
import org.g2.inv.calculate.app.handler.transaction.AbstractTransactionHandler;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 增量库存处理
 *
 * @author wuwenxi 2022-04-11
 */
@Component
public class IncrTransactionHandler extends AbstractTransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(IncrTransactionHandler.class);

    public IncrTransactionHandler(StockLevelRepository stockLevelRepository) {
        super(stockLevelRepository);
    }

    @Override
    public void handler(String posCode, List<InvTransaction> transactions) {
        Map<String, List<InvTransaction>> groupMap = transactions.stream()
                .peek(e -> e.setProcessingStatusCode(CoreConstants.ProcessStatus.SUCCESS))
                .collect(Collectors.groupingBy(InvTransaction::getSkuCode));
        for (Map.Entry<String, List<InvTransaction>> entry : groupMap.entrySet()) {
            String key = entry.getKey();
            List<InvTransaction> value = entry.getValue();
            try {
                StockLevel stockLevel = selectOneBySkuAndPos(key, posCode);
            } catch (Exception e) {
                String errMsg = StringUtil.exceptionString(e);
                log.error("处理增量存事务发生异常,skuCode: {},posCode:{}, 异常信息:{}", key, posCode, errMsg);
                for (InvTransaction invTransaction : value) {
                    invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.ERROR);
                    invTransaction.setErrorMsg(errMsg);
                }
            }
        }
    }

    @Override
    public String type() {
        return InvCalculateConstants.TransactionType.INCREMENT;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}