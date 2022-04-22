package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 库存上报
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_report")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockReport extends AuditDomain implements Serializable {

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
}
