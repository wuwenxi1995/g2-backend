package org.g2.inv.calculate.app.handler.transaction.impl;

import org.g2.inv.calculate.app.handler.transaction.AbstractTransactionHandler;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wuwenxi 2022-04-11
 */
@Component
public class DefaultTransactionHandler extends AbstractTransactionHandler {

    @Override
    public String transactionType() {
        return InvCalculateConstants.TransactionType.DEFAULT_TYPE;
    }

    @Override
    protected Object handler(String posCode, List<InvTransaction> transactions) {
        return null;
    }
}
