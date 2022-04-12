package org.g2.inv.calculate.app.handler.transaction.impl;

import org.g2.inv.calculate.app.handler.transaction.AbstractTransactionHandler;
import org.g2.inv.calculate.domain.repository.TransactionOperationRepository;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wuwenxi 2022-04-11
 */
@Component
public class IncrementAmountTransactionHandler extends AbstractTransactionHandler {

    public IncrementAmountTransactionHandler(TransactionOperationRepository transactionOperationRepository,
                                             StockLevelRepository stockLevelRepository) {
        super(stockLevelRepository, transactionOperationRepository);
    }

    @Override
    public String transactionType() {
        return InvCalculateConstants.TransactionType.INCREMENT;
    }

    @Override
    public void handler(String posCode, List<InvTransaction> transactions) {
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
