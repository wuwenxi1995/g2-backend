package org.g2.inv.calculate.domain.repository;

import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;

import java.util.List;

/**
 * @author wuwenxi 2022-04-12
 */
public interface TransactionOperationRepository {

    /**
     * 持久化数据
     *
     * @param stockLevel     库存信息
     * @param invTransaction 库存事务
     * @param isCreate       是否新建库存
     */
    void persistence(StockLevel stockLevel, InvTransaction invTransaction, boolean isCreate);

    /**
     * 持久化数据
     *
     * @param stockLevel      库存信息
     * @param invTransactions 库存事务
     * @param isCreate        是否新建库存
     */
    void persistence(StockLevel stockLevel, List<InvTransaction> invTransactions, boolean isCreate);

    /**
     * 更新库存事务信息
     *
     * @param invTransactions 库存事务
     * @param fields          更新字段
     */
    void updateOperational(List<InvTransaction> invTransactions, String... fields);

    /**
     * 更新库存事务信息
     *
     * @param invTransaction 库存事务
     * @param fields         更新字段
     */
    void updateOperational(InvTransaction invTransaction, String... fields);
}
