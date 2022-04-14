package org.g2.inv.calculate.app.trigger;

import org.g2.inv.trigger.domain.vo.TriggerMessage;

/**
 * @author wuwenxi 2022-04-14
 */
public interface TriggerMessageHandler {

    /**
     * 处理消息
     *
     * @param triggerMessage 消息内容
     */
    void handler(TriggerMessage triggerMessage);

    /**
     * 处理类型
     *
     * @return 处理类型
     */
    String type();
}
