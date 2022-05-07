package org.g2.inv.calculate.infra.message.handler;

import org.g2.inv.core.domain.vo.TriggerMessage;

import java.util.List;

/**
 * 消息处理器
 *
 * @author wuwenxi 2022-05-06
 */
public interface MessageHandler {

    /**
     * 处理消息
     *
     * @param triggerMessages 消息内容
     */
    void handler(List<TriggerMessage> triggerMessages);

    /**
     * 处理topic消息
     *
     * @return topic
     */
    String topic();
}
