package org.g2.inv.core.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

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
        /**
         * format
         * <p>
         * g2inv:stock:posCode:
         * </p>
         */
        String STOCK_LEVEL_FORMAT = "g2inv:stock:%s:";
    }

    interface InvStockKey {
        /**
         * 库存量
         */
        String HASH_STOCK_LEVEL = "stock_level";
        /**
         * 库存保留量
         */
        String HASH_EXTEND_STOCK_RESERVED = "extend_reserved";
        /**
         * 库存可用量
         */
        String HASH_EXTEND_STOCK_ATS = "extend_ats";
    }

    interface RedisScript {
        ResourceScriptSource POS_STOCK_OCCUPY_SCRIPT = new ResourceScriptSource(new ClassPathResource("script/lua/pos-stock-occupy.lua"));
        ResourceScriptSource POS_STOCK_RELEASE_SCRIPT = new ResourceScriptSource(new ClassPathResource("script/lua/pos-stock-release.lua"));
        ResourceScriptSource POS_STOCK_REDUCE_SCRIPT = new ResourceScriptSource(new ClassPathResource("script/lua/pos-stock-reduce.lua"));
    }

    interface MessageQueue {
        String TRIGGER_POS_STOCK_KEY = "g2inv:queue:trigger_pos_stock";
    }
}
