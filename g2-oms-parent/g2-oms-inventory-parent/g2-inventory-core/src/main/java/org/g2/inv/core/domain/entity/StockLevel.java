package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.choerodon.mybatis.domain.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 *  服务点库存明细
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_level")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockLevel extends AuditDomain {

    @Id
    @GeneratedValue
    private Long stockLevelId;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 服务点编码
     */
    private String posCode;
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
