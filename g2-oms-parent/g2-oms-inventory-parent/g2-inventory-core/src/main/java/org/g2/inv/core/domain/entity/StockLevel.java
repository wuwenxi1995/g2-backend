package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 库存明细
 *
 * @author wuwenxi 2022-04-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "g2inv_stock_level")
public class StockLevel extends AuditDomain {

    private static final String FILED_STOCK_LEVEL_ID = "stockLevelId";
    private static final String FILED_SKU_CODE = "skuCode";
    private static final String FILED_POS_CODE = "posCode";
    private static final String FILED_INITIAL_ATS = "initialAts";
    private static final String FILED_ON_HAND = "onHand";
    private static final String FILED_RESERVED = "reserved";
    private static final String FILED_LAST_MODIFIED_TIME = "lastModifiedTime";
    private static final String FILED_LAST_FULL_TIME = "lastFullTime";

    /**
     * 库存明细id
     */
    @Id
    @GeneratedValue
    private Long stockLevelId;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 门店编码
     */
    private String posCode;
    /**
     * 初始库存量
     */
    private Long initialAts;
    /**
     * 现有量
     */
    private Long onHand;
    /**
     * 保留量
     */
    private Long reserved;
    /**
     * 最后一次更新时间
     */
    private Date lastModifiedTime;
    /**
     * 最后一个全量时间
     */
    private Date lastFullTime;
}
