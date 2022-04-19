package org.g2.inv.trigger.app.service;

/**
 * @author wuwenxi 2022-04-08
 */
public interface InvCalculateTriggerService extends TriggerInvTransactionService, TransactionTriggerService {

    /**
     * 触发库存计算
     *
     * @param triggerData 触发数据
     * @param type        触发类型
     * @param queue       数据队列
     * @param <T>         数据类型
     */
    <T> void trigger(T triggerData, String type, String queue);
}
