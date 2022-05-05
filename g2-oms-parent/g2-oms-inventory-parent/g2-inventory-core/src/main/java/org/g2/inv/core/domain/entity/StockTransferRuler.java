package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 库存调拨规则
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_transfer_rule")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockTransferRuler extends AuditDomain {

    @Id
    @GeneratedValue
    private Long stockTransferRulerId;
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
    private Long safetyQuantity;
    /**
     * 是否允许调拨库存
     */
    private Integer enableTransfer;
    /**
     * 调拨规则状态
     */
    private String transferStatusCode;
}
