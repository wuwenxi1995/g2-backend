package org.g2.inv.calculate.app.handler.transaction;

import org.g2.core.chain.handler.ParamChainInvocationHandler;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * @author wuwenxi 2022-04-11
 */
public interface TransactionHandler extends ParamChainInvocationHandler {

    /**
     * 库存事务类型
     *
     * @return 事务类型
     */
    String transactionType();
}
