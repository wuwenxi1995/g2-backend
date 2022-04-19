package org.g2.inv.calculate.infra.constant;

/**
 * @author wuwenxi 2022-04-08
 */
public interface InvCalculateConstants {
    interface TriggerType {
        String POS_TRANSACTION_TRIGGER = "TRIGGER_TRANSACTION";
        String POS_DEFAULT_TRIGGER = "DEFAULT";
    }

    interface RedisKey {
        String INVENTORY_TRANSACTION_KEY = "g2inv:queue:transaction";
        String TRIGGER_POS_STOCK_KEY = "g2inv:queue:trigger_pos_stock";
    }

    interface TransactionType {
        String DEFAULT_TYPE = "default";
        String FULL = "full";
        String INCREMENT = "increment";
    }
}
