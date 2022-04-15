package org.g2.inv.calculate.app.trigger.pos.impl;

import org.g2.inv.calculate.app.trigger.pos.PosStockTriggerHandler;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.trigger.domain.vo.TriggerMessage;
import org.springframework.stereotype.Component;

/**
 * @author wuwenxi 2022-04-15
 */
@Component
public class DefaultPosStockTriggerHandler implements PosStockTriggerHandler {

    @Override
    public void handler(TriggerMessage triggerMessage) {

    }

    @Override
    public String type() {
        return InvCalculateConstants.TriggerType.POS_DEFAULT_TRIGGER;
    }
}
