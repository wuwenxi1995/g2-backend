package org.g2.inv.api.dto.pos;

import lombok.Data;

import java.util.Map;

/**
 * 服务点库存操作
 *
 * @author wuwenxi 2022-04-15
 */
@Data
public class PosStockOperateDTO {

    /**
     * 服务点库存
     */
    private String posCode;
    /**
     * sku-quantity
     */
    private Map<String, Long> items;
}
