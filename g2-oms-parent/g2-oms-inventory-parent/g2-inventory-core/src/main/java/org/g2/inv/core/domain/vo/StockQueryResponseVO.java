package org.g2.inv.core.domain.vo;

import lombok.Data;

/**
 * @author wuwenxi 2022-04-15
 */
@Data
public class StockQueryResponseVO {

    /**
     * 可用量
     */
    private boolean available;

    /**
     * 平台sku编码
     */
    private String skuCode;
}
