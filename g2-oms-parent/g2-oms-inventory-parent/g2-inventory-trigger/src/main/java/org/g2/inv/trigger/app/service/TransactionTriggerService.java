package org.g2.inv.trigger.app.service;

import java.util.List;

/**
 * @author wuwenxi 2022-05-05
 */
public interface TransactionTriggerService {

    /**
     * 触发库存事务消费
     *
     * @param data 库存事务信息
     * @param <T>  T
     */
    <T> void transactionTrigger(T data);

    /**
     * 触发库存事务消费
     *
     * @param data      库存事务信息
     * @param topic     kafka队列
     * @param partition 分区id
     * @param key       kafka可能需要用来计算推送分区的key
     * @param <T>       T
     */
    <T> void transactionTrigger(List<T> data, String topic, Integer partition, String key);
}
