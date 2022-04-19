package org.g2.inv.trigger.infra.constant;

/**
 * @author wuwenxi 2022-04-08
 */
public interface InvTriggerConstants {

    interface TriggerQueue {
        String TRIGGER_POS_STOCK = "g2inv:queue:trigger_pos_stock";
        String INVENTORY_TRANSACTION_KEY = "g2inv:queue:transaction";
    }

    interface TriggerType {
        String TRANSACTION_TRIGGER = "TRIGGER_TRANSACTION";
    }
}
