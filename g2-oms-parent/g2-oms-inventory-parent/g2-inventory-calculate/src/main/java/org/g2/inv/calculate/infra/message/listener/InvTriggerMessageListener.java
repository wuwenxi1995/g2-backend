package org.g2.inv.calculate.infra.message.listener;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.g2.starter.core.util.CollectionUtils;
import org.g2.inv.calculate.infra.message.handler.MessageHandler;
import org.g2.inv.core.domain.vo.TriggerMessage;
import org.g2.inv.core.infra.constant.InvCoreConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-05-05
 */
@Component
public class InvTriggerMessageListener {

    private final MessageHandler messageHandler;

    public InvTriggerMessageListener(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * kafka监听
     *
     * @param consumerRecords 消息内容
     * @param ack             当ackMode为manual或manual_immediate时不为空，否则为空；用于手动提交offset
     */
    @KafkaListener(topics = {InvCoreConstants.TriggerTopic.TRANSACTION_CONSUMER}, groupId = "transaction")
    public void transactionListener(List<ConsumerRecord<String, String>> consumerRecords, Acknowledgment ack) {
        List<TriggerMessage> triggerMessages = consumerRecords.stream()
                .map(record -> JSONObject.parseObject(record.value(), TriggerMessage.class))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(triggerMessages)) {
            this.messageHandler.handler(triggerMessages);
        }
        ack.acknowledge();
    }
}
