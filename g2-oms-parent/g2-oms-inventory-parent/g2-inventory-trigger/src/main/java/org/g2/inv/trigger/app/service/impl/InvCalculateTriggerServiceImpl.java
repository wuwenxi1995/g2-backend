package org.g2.inv.trigger.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.inv.trigger.app.service.InvCalculateTriggerService;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.g2.inv.trigger.domain.vo.TriggerMessage;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.g2.inv.trigger.infra.constant.InvTriggerConstants.*;

/**
 * @author wuwenxi 2022-04-08
 */
public class InvCalculateTriggerServiceImpl implements InvCalculateTriggerService {

    private final DynamicRedisHelper redisHelper;

    public InvCalculateTriggerServiceImpl(DynamicRedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @Override
    public void triggerCalculate(String transactionCode) {
        if (StringUtils.isBlank(transactionCode)) {
            return;
        }
        this.trigger(transactionCode, null, TriggerQueue.INVENTORY_TRANSACTION_KEY);
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
        this.trigger(triggerList, TriggerType.TRANSACTION_TRIGGER, TriggerQueue.TRIGGER_POS_STOCK);
    }

    @Override
    public <T> void trigger(T triggerData, String type, String queue) {
        TriggerMessage<T> message = new TriggerMessage<>();
        message.setContent(triggerData);
        message.setType(type);
        message.setTriggerDate(new Date());
        redisHelper.setCurrentDataBase(0);
        try {
            redisHelper.lstRightPush(queue, JSONObject.toJSONString(message));
        } finally {
            redisHelper.clearCurrentDataBase();
        }
    }
}
