package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 区域库存明细
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_region_stock_level")
@EqualsAndHashCode(callSuper = true)
@Data
public class RegionStockLevel extends AuditDomain {

    @Id
    @GeneratedValue
    private Long regionStockLevelId;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 区域编码
     */
    private String regionCode;
    /**
     * 现有库存
     */
    private Long onHand;
    /**
     * 配货库存
     */
    private Long consignment;
    /**
     * 服务点调拨库存
     */
    private Long transfer;
    /**
     * 服务点预留库存
     */
    private Long reserved;
    /**
     * 服务点不可售库存
     */
    private Long unmerchantable;
    /**
     * 虚拟库存(不计入总库存)
     */
    private Long virtual;
    /**
     * 最近一次更新时间
     */
    private Date latsModifyDate;
}
