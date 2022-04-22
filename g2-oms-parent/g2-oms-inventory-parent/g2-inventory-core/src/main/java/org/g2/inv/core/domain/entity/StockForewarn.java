package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存预警
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_forewarn")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockForewarn extends AuditDomain {

    @Id
    @GeneratedValue
    private Long stockForewarnId;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 服务点编码
     */
    private String posCode;
    /**
     * 库存安全比例
     */
    private BigDecimal safetyRatio;
    /**
     * 有效时间从
     */
    private Date dateFrom;
    /**
     * 有效时间至
     */
    private Date dateTo;
    /**
     * 库存预警状态
     */
    private String forewarnStatusCode;
}
