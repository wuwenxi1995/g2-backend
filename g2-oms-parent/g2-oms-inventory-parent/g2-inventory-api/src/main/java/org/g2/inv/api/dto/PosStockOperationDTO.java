package org.g2.inv.api.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author wuwenxi 2022-04-18
 */
@Data
public class PosStockOperationDTO {

    private String token;
    private Item content;

    @Data
    private static class Item {
        /**
         * 服务点库存
         */
        private String posCode;
        /**
         * sku-quantity
         */
        private Map<String, Long> items;
    }
}
