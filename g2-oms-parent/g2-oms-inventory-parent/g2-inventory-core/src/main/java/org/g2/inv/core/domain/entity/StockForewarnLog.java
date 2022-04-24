package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.choerodon.mybatis.domain.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 库存预警日志
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_forewarn_log")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockForewarnLog extends AuditDomain {

    @Id
    @GeneratedValue
    private Long stockForewarnLogId;
    /**
     * 库存预警id
     */
    private Long stockForewarnId;
    /**
     * 预警消息
     */
    private String message;
    /**
     * 触发时间
     */
    private Date triggerDate;
    /**
     * 触发成功
     */
    private Integer triggerSuccess;
}
