package org.g2.inv.calculate.infra.message.handler.impl;

import org.g2.inv.calculate.app.handler.trigger.TransactionTriggerHandler;
import org.g2.inv.calculate.infra.message.handler.MessageHandler;
import org.g2.inv.core.domain.vo.TriggerMessage;
import org.g2.inv.core.infra.constant.InvCoreConstants;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-05-06
 */
@Component
public class TransactionMessageHandler implements MessageHandler {

    private final Map<String, TransactionTriggerHandler> handlerMap;

    public TransactionMessageHandler(List<TransactionTriggerHandler> transactionHandlers) {
        this.handlerMap = transactionHandlers.stream().collect(Collectors.toMap(TransactionTriggerHandler::type, Function.identity()));
    }

    @Override
    public void handler(List<TriggerMessage> triggerMessages) {
        Map<String, List<TriggerMessage>> groupByType = triggerMessages.stream().collect(Collectors.groupingBy(TriggerMessage::getTriggerType));
        groupByType.forEach((key, value) -> {
            TransactionTriggerHandler triggerHandler = handlerMap.get(key);
            triggerHandler.handler(value);
        });
    }

    @Override
    public String topic() {
        return InvCoreConstants.TriggerTopic.TRANSACTION_CONSUMER;
    }
}
