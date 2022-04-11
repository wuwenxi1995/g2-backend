package org.g2.inv.trigger.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.g2.core.util.StringUtil;
import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Collections;
import java.util.List;

import static org.g2.inv.trigger.infra.constant.InvTriggerConstants.*;

/**
 * @author wuwenxi 2022-04-08
 */
public class InvCalculateTriggerServiceImpl implements InvCalculateTriggerService {
    private static final Logger log = LoggerFactory.getLogger(InvCalculateTriggerServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public InvCalculateTriggerServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void triggerByTransaction(TransactionTriggerVO trigger) {
        if (trigger == null) {
            return;
        }
        this.triggerByTransaction(Collections.singletonList(trigger));
    }

    @Override
    public void triggerByTransaction(List<TransactionTriggerVO> triggerList) {
        if (CollectionUtils.isEmpty(triggerList)) {
            return;
        }
        this.trigger(triggerList, TriggerTopic.TOPIC_TRANSACTION_TRIGGER, null, TriggerType.TRANSACTION_TRIGGER);
    }

    @Override
    public <T> void trigger(List<T> triggerData, String topic, Integer partition, String key) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, partition, key, JSONObject.toJSONString(triggerData));
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(@NonNull Throwable ex) {
                log.error("kafka推送数据失败, 推送key:{}, 异常信息:{}", key, StringUtil.exceptionString(ex));
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("kafka推送数据成功, 推送key:{}", key);
            }
        });
    }
}
