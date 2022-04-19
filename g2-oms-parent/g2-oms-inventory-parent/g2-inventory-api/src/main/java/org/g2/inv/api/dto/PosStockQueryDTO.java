package org.g2.inv.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author wuwenxi 2022-04-18
 */
@Data
public class PosStockQueryDTO {
    /**
     * 服务点编码
     */
    private String posCode;
    /**
     * 查询项
     */
    private List<PosStockQueryItemDTO> items;

    @Data
    private static class PosStockQueryItemDTO {
        /**
         * sku编码
         */
        private String masterSkuCode;
        /**
         * 查询数量
         */
        private Long quantity;
    }
}
