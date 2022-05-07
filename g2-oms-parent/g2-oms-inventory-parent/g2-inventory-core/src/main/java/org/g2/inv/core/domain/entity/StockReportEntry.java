package org.g2.inv.core.domain.entity;

import org.g2.starter.mybatis.entity.AuditDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_report_entry")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockReportEntry extends AuditDomain {

    public static final String FIELD_STOCK_REPORT_ENTRY_ID = "stockReportEntryId";
    public static final String FIELD_REPORT_CODE = "reportCode";
    public static final String FIELD_SKU_CODE = "skuCode";
    public static final String FIELD_POS_CODE = "posCode";
    public static final String FIELD_QUANTITY = "quantity";

    @Id
    @GeneratedValue
    private Long stockReportEntryId;

    /**
     * 关联inv_stock_report
     */
    private String reportCode;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 服务点编码
     */
    private String posCode;

    /**
     * 上报数量
     */
    private Long quantity;

    /**
     * 上报类型, 增量/全量
     */
    private String reportType;
}
