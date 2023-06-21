package org.g2.inv.calculate.app.handler.transaction;

import org.g2.starter.core.chain.handler.ParamChainInvocationHandler;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.infra.constant.InvCoreConstants;

import java.util.List;

/**
 * @author wuwenxi 2022-05-07
 */
public interface TransactionHandler extends ParamChainInvocationHandler {

    /**
     * 根据服务点编码处理库存事务
     *
     * @param posCode         服务点拜纳姆
     * @param invTransactions 库存事务
     */
    void handler(String posCode, List<InvTransaction> invTransactions);

    /**
     * 库存事务类型{@link InvCoreConstants.TransactionTypeCode}
     *
     * @return 库存事务类型
     */
    String type();
}
