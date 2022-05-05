package org.g2.inv.core.domain.entity;

import org.g2.starter.mybatis.entity.AuditDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 库存上报
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_report")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockReport extends AuditDomain {

    public static final String FIELD_INV_STOCK_REPORT_ID = "invStockReportId";
    public static final String FIELD_REPORT_CODE = "reportCode";
    public static final String FIELD_REPORT_STATUS_CODE = "reportStatusCode";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_REPORT_DATE = "reportDate";
    public static final String FIELD_SUBMIT_DATE = "submitDate";

    @Id
    @GeneratedValue
    private Long invStockReportId;

    /**
     * 批次号
     */
    private String reportCode;

    /**
     * 上报状态
     */
    private String reportStatusCode;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 上报时间
     */
    private Date reportDate;

    /**
     * 库存上报提交时间
     */
    private Date submitDate;

    @Transient
    private List<StockReportEntry> stockReportEntryList;
}
