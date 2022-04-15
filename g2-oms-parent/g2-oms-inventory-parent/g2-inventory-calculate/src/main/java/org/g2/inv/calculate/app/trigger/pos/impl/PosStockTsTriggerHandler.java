package org.g2.inv.calculate.app.trigger.pos.impl;

import org.g2.inv.calculate.app.trigger.pos.PosStockTriggerHandler;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.PosStock;
import org.g2.inv.core.domain.repository.PosStockRepository;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.g2.inv.trigger.domain.vo.TriggerMessage;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wuwenxi 2022-04-14
 */
@Component
public class PosStockTsTriggerHandler implements PosStockTriggerHandler {

    private final PosStockRepository posStockRepository;

    public PosStockTsTriggerHandler(PosStockRepository posStockRepository) {
        this.posStockRepository = posStockRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handler(TriggerMessage triggerMessage) {
        List<TransactionTriggerVO> triggers = (List<TransactionTriggerVO>) triggerMessage.getContent();
        for (TransactionTriggerVO trigger : triggers) {
            PosStock query = new PosStock();
            query.setPosCode(trigger.getPosCode());
            query.setSkuCode(trigger.getMasterSkuCode());
            PosStock posStock = posStockRepository.selectOne(query);
            if (posStock == null) {
                posStock = new PosStock();
                posStock.setPosCode(trigger.getPosCode());
                posStock.setSkuCode(trigger.getMasterSkuCode());
                posStock.setAts(0L);
                posStock.setLastTriggerTime(triggerMessage.getTriggerDate());
            }
        }
    }

    @Override
    public String type() {
        return InvCalculateConstants.TriggerType.POS_TRANSACTION_TRIGGER;
    }
}
