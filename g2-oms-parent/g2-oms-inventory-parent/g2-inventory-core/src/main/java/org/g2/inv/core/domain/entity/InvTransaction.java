package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_transaction")
@EqualsAndHashCode(callSuper = true)
@Data
public class InvTransaction extends AuditDomain {

    @Id
    @GeneratedValue
    private Long invTransactionId;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 服务点编码
     */
    private String posCode;
    /**
     * 事务类型
     */
    private String transactionType;
    /**
     * 事务来源
     */
    private String transactionSource;
    /**
     * 事务编码
     */
    private String transactionCode;
    /**
     * 来源类型
     */
    private String sourceType;
    /**
     * 事务创建时间
     */
    private Date sourceDate;
    /**
     * 库存增量
     */
    private Long onHandInc;
    /**
     * 库存保留量
     */
    private Long reservedInc;
    /**
     * 库存事务处理状态
     */
    private String processStatusCode;
    /**
     * 库存事务处理信息
     */
    private String errorMsg;
}
