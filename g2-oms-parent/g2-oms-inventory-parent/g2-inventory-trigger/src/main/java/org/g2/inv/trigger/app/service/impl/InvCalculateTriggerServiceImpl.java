package org.g2.inv.trigger.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.g2.core.util.StringUtil;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;

import static org.g2.inv.trigger.infra.constant.InvTriggerConstants.*;

/**
 * @author wuwenxi 2022-04-08
 */
public class InvCalculateTriggerServiceImpl implements InvCalculateTriggerService {
    private static final Logger log = LoggerFactory.getLogger(InvCalculateTriggerServiceImpl.class);

    private final DynamicRedisHelper redisHelper;

    public InvCalculateTriggerServiceImpl(DynamicRedisHelper redisHelper) {
        this.redisHelper = redisHelper;
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
    }
}
