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
 * 服务点库存
 *
 * @author wuwenxi 2022-04-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "g2inv_pos_stock")
public class PosStock extends AuditDomain {

    @Id
    @GeneratedValue
    private Long posStockId;
    /**
     * 服务点编码
     */
    private String posCode;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * spu编码
     */
    private String spuCode;
    /**
     * 服务点库存安全比例
     */
    private BigDecimal safeRatio;
    /**
     * 安全库存
     */
    private Long safetyStock;
    /**
     * 库存可用量
     */
    private Long ats;
    /**
     * 保留量
     */
    private Long reserved;
    /**
     * 最近一次触发服务点库存计算时间
     */
    private Date lastTriggerTime;
}
