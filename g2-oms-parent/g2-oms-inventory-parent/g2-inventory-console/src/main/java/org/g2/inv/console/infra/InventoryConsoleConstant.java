package org.g2.inv.console.infra;

/**
 * @author wuwenxi 2022-04-11
 */
public interface InventoryConsoleConstant {

    interface RedisKey {
        String INVENTORY_TRANSACTION_KEY = "g2inv:transaction:queue";
    }
}
