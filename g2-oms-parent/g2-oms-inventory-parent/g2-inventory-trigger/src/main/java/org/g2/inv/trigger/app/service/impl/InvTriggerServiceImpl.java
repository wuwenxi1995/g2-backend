package org.g2.inv.trigger.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.g2.starter.core.util.StringUtil;
import org.g2.inv.core.domain.vo.TriggerMessage;
import org.g2.inv.core.infra.constant.InvCoreConstants;
import org.g2.inv.trigger.app.service.InvTriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Collections;
import java.util.List;

/**
 * @author wuwenxi 2022-05-05
 */
@Service
public class InvTriggerServiceImpl implements InvTriggerService {

    private static final Logger log = LoggerFactory.getLogger(InvTriggerServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public InvTriggerServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public <T> void transactionTrigger(T data) {
        this.transactionTrigger(Collections.singletonList(data), InvCoreConstants.TriggerTopic.TRANSACTION_CONSUMER, null, null);
    }

    @Override
    public <T> void transactionTrigger(List<T> data, String topic, Integer partition, String key) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        TriggerMessage<List<T>> triggerMessage = new TriggerMessage<>(data, InvCoreConstants.TriggerType.TRANSACTION_TRIGGER);
        this.trigger(triggerMessage, topic, partition, key);
    }

    @Override
    public <T> void trigger(TriggerMessage<T> tTriggerMessage, String topic, Integer partition, String key) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, partition, key, JSONObject.toJSONString(tTriggerMessage));
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("kafka推送数据成功, 推送topic:{}, 消息类型：{}", topic, tTriggerMessage.getTriggerType());
            }

            @Override
            public void onFailure(@NonNull Throwable ex) {
                log.error("kafka推送数据失败, 推送topic:{}, 消息类型：{}, 异常信息:{}", topic, tTriggerMessage.getTriggerType(), StringUtil.exceptionString(ex));
            }
        });
    }

}
