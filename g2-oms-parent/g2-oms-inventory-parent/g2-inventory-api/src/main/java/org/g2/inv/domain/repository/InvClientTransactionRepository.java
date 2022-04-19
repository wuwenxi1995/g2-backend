package org.g2.inv.domain.repository;

import org.g2.inv.core.domain.entity.InvTransaction;

import java.util.List;

/**
 * @author wuwenxi 2022-04-18
 */
public interface InvClientTransactionRepository {

    /**
     * 保存库存事务
     *
     * @param invTransaction 库存事务
     */
    void saveInvTransaction(InvTransaction invTransaction);

    /**
     * 批量保存库存事务
     *
     * @param invTransactions 库存事务
     */
    void saveInvTransactions(List<InvTransaction> invTransactions);
}
