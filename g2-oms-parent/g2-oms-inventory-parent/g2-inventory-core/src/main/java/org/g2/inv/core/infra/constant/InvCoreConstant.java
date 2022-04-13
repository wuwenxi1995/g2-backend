package org.g2.inv.core.infra.constant;

/**
 * @author wuwenxi 2022-04-13
 */
public interface InvCoreConstant {

    interface RedisKeyFormat {
        /**
         * 库存信息，采用hash存储库存可用量、reserved、ats
         * <p>
         * g2inv:stock:posCode:skuCode
         * </p>
         */
        String STOCK_LEVEL_KEY = "g2inv:stock:%s:%s";
    }
}
