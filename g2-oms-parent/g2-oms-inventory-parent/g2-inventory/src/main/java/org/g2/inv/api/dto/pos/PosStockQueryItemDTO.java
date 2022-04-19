package org.g2.inv.api.dto.pos;

import lombok.Data;

/**
 * @author wuwenxi 2022-04-15
 */
@Data
public class PosStockQueryItemDTO {

    /**
     * sku编码
     */
    private String masterSkuCode;
    /**
     * 查询数量
     */
    private Long quantity;

}
