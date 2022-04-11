package org.g2.inv.calculate.infra.constant;

/**
 * @author wuwenxi 2022-04-08
 */
public interface InvCalculateConstants {

    interface TriggerTopic {
        String TOPIC_TRANSACTION_TRIGGER = "inv_trigger_transaction";
    }

    interface TriggerType {
        String TRANSACTION_TRIGGER = "TRIGGER_TRANSACTION";
    }

    interface RedisKey {
        String INVENTORY_TRANSACTION_KEY = "g2inv:transaction:queue";
    }

    interface TransactionType {
        String DEFAULT_TYPE = "default";
        String FULL = "full";
        String INCREMENT = "increment";
        String CONSIGNMENT = "consignment";
    }
}
