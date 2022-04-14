package org.g2.inv.trigger.app.service;

/**
 * @author wuwenxi 2022-04-14
 */
public interface TriggerInvTransactionService {

    /**
     * 触发库存计算
     *
     * @param transactionCode 库存事务编码
     */
    void triggerCalculate(String transactionCode);
}
