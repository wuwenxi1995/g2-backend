package org.g2.inv.core.domain.repository;

import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.starter.mybatis.base.BaseRepository;

import java.util.List;

/**
 * @author wuwenxi 2022-04-22
 */
public interface InvTransactionRepository extends BaseRepository<InvTransaction> {

    /**
     * 通过事务编码查询库存事务
     *
     * @param transactionCodes 库存事务编码
     * @return 库存事务明细
     */
    List<InvTransaction> findByCodes(List<String> transactionCodes);
}
