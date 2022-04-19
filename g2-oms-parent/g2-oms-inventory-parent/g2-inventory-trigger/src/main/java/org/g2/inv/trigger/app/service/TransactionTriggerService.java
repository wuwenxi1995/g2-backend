package org.g2.inv.trigger.app.service;

import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;

import java.util.List;

/**
 * 触发事务库存计算
 *
 * @author wuwenxi 2022-04-08
 */
public interface TransactionTriggerService {

    /**
     * 触发库存事务计算
     *
     * @param trigger 触发数据
     */
    void triggerByTransaction(TransactionTriggerVO trigger);

    /**
     * 触发库存事务计算
     *
     * @param triggerList 触发数据
     */
    void triggerByTransaction(List<TransactionTriggerVO> triggerList);
}
