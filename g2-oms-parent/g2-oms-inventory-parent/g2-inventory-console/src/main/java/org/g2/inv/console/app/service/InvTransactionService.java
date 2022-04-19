package org.g2.inv.console.app.service;

import org.g2.inv.core.domain.entity.InvTransaction;

import java.util.List;

/**
 * @author wuwenxi 2022-04-09
 */
public interface InvTransactionService {

    /**
     * 列表查询
     *
     * @param invTransaction 查询条件
     * @return 库存事务列表
     */
    List<InvTransaction> list(InvTransaction invTransaction);

    /**
     * 查询库存事务详情
     *
     * @param invTransactionId 库存事务id
     * @return 事务详情
     */
    InvTransaction detail(Long invTransactionId);

    /**
     * 新增库存事务
     *
     * @param invTransactions 库存事务
     */
    void create(List<InvTransaction> invTransactions);

    /**
     * 上传库存事务
     *
     * @param invTransactions 库存事务
     */
    void upload(List<InvTransaction> invTransactions);
}
