package org.g2.inv.calculate.infra.message.handler;

import org.g2.inv.calculate.app.handler.trigger.TriggerHandler;
import org.g2.inv.core.domain.vo.TriggerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息处理器
 *
 * @author wuwenxi 2022-05-06
 */
@Component
public class MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private final Map<String, TriggerHandler> handlerMap;

    public MessageHandler(List<TriggerHandler> transactionHandlers) {
        this.handlerMap = transactionHandlers.stream().collect(Collectors.toMap(TriggerHandler::type, Function.identity()));
    }

    public void handler(List<TriggerMessage> triggerMessages) {
        Map<String, List<TriggerMessage>> groupByType = triggerMessages.stream().collect(Collectors.groupingBy(TriggerMessage::getTriggerType));
        groupByType.forEach((key, value) -> {
            TriggerHandler triggerHandler = handlerMap.get(key);
            if (triggerHandler == null) {
                log.warn("messageHandler not found triggerHandler, triggerType : {}", key);
                return;
            }
            triggerHandler.handler(value);
        });
    }
}
