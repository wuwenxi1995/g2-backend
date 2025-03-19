package org.g2.inv.core.infra.constant;

/**
 * @author wuwenxi 2022-04-24
 */
public interface InvCoreConstants {

    interface TransactionTypeCode {
        String FULL = "FULL";
        String INCREMENT = "INCREMENT";
        String OCCUPY = "OCCUPY";
        String CONSIGNMENT = "CONSIGNMENT";
        String TRANSFER = "transfer";
        String RESERVED = "reserved";
        String UN_MERCHANTABLE = "unmerchantable";
    }

    interface TransactionSource {
        String API = "API";
        String MANUAL = "MANUAL";
        String ERP = "ERP";
        String POS = "POS";
        String OMS = "OMS";
        String DEFAULT = TransactionSource.MANUAL;
    }

    interface TriggerTopic {
        /**
         * 触发库存事务消费
         */
        String TRANSACTION_CONSUMER = "TRANSACTION";
    }

    interface TriggerType {
        String TRANSACTION_TRIGGER = "TRIGGER_TRANSACTION";
    }
}
