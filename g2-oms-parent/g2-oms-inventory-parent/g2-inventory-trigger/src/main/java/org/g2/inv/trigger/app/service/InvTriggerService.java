package org.g2.inv.trigger.app.service;

import org.g2.inv.core.domain.vo.TriggerMessage;

/**
 * @author wuwenxi 2022-05-05
 */
public interface InvTriggerService extends
        TransactionTriggerService {

    /**
     * 推入kafka队列中
     *
     * @param tTriggerMessage 触发信息
     * @param topic           kafka队列
     * @param partition       topic对应的分区id
     * @param key             kafka可能需要用来计算推送分区的key
     * @param <T>             T
     */
    <T> void trigger(TriggerMessage<T> tTriggerMessage, String topic, Integer partition, String key);
}
