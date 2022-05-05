package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 库存调拨规则
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_transfer")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockTransfer extends AuditDomain {

    @Id
    @GeneratedValue
    private Long stockTransferId;
    /**
     * 服务点编码
     */
    private String posCode;
    /**
     * 商品编码
     */
    private String skuCode;
    /**
     * 库存安全值
     */
    private Long quantity;
}
