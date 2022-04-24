package org.g2.inv.core.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author wuwenxi 2022-04-24
 */
@Data
public class StockReportDTO {

    /**
     * 供应商编码
     */
    private String supplierCode;
    /**
     * 上报编码
     */
    private String reportCode;
    /**
     * 上报状态
     */
    private String reportStatusCode;
    /**
     * 上报时间从和至
     */
    private Date reportDateFrom;
    private Date reportDateTo;
    /**
     * 提交时间从和至
     */
    private Date submitDateFrom;
    private Date submitDateTo;
}
