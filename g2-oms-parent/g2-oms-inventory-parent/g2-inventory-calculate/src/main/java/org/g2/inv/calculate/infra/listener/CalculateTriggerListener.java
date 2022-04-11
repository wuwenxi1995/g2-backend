package org.g2.inv.calculate.infra.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.g2.inv.calculate.infra.constant.InvCalculateConstants.*;

/**
 * @author wuwenxi 2022-04-08
 */
@Component
public class CalculateTriggerListener {

    @KafkaListener(topics = {TriggerTopic.TOPIC_TRANSACTION_TRIGGER})
    public void transactionCalculateListener(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        Optional<?> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            try {
                String data = (String) message.get();
            } finally {
                ack.acknowledge();
            }
        }
    }
}
