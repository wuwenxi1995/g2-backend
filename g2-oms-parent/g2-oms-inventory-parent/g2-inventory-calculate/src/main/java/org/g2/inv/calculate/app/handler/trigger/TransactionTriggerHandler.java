package org.g2.inv.calculate.app.handler.trigger;

import org.g2.inv.core.domain.vo.TriggerMessage;

import java.util.List;

/**
 * @author wuwenxi 2022-05-07
 */
public interface TransactionTriggerHandler {

    /**
     * 处理数据
     *
     * @param triggerMessages 触发信息
     */
    void handler(List<TriggerMessage> triggerMessages);

    /**
     * 触发类型
     *
     * @return type
     */
    String type();
}
