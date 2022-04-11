package org.g2.inv.trigger.app.service;

import java.util.List;

/**
 * @author wuwenxi 2022-04-08
 */
public interface InvCalculateTriggerService extends TransactionTriggerService {

    /**
     * 触发库存计算
     *
     * @param triggerData 触发数据
     * @param topic       kafka topic
     * @param partition   分区id
     * @param key         触发类型
     * @param <T>         数据类型
     */
    <T> void trigger(List<T> triggerData, String topic, Integer partition, String key);
}
