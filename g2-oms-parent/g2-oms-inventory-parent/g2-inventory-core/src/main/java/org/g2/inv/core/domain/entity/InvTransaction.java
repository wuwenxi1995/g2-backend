package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 库存事务明细
 *
 * @author wuwenxi 2022-04-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "g2inv_transaction")
public class InvTransaction extends AuditDomain {

    public static final String FILED_INV_TRANSACTION_ID = "invTransactionId";
    public static final String FILED_TRANSACTION_CODE = "transactionCode";
    public static final String FILED_TRANSACTION_TYPE = "transactionType";
    public static final String FILED_TRANSACTION_SOURCE = "transactionSource";
    public static final String FILED_SOURCE_DATE = "sourceDate";
    public static final String FILED_PROCESSING_STATUS_CODE = "processingStatusCode";
    public static final String FILED_SKU_CODE = "skuCode";
    public static final String FILED_POS_CODE = "posCode";
    public static final String FILED_ON_HAND_INV = "onHandInv";
    public static final String FILED_RESERVE_INV = "reserveInv";
    public static final String FILED_ERROR_MSG = "errorMsg";

    /**
     * 库存事务id
     */
    @Id
    @GeneratedValue
    private Long invTransactionId;
    /**
     * 库存事务编码
     */
    private String transactionCode;
    /**
     * 库存事务类型
     */
    private String transactionType;
    /**
     * 库存事务来源
     */
    private String transactionSource;
    /**
     * 创建时间
     */
    private Date sourceDate;
    /**
     * 处理状态
     */
    private String processingStatusCode;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 门店编码
     */
    private String posCode;
    /**
     * 现有量增量
     */
    private Long onHandInv;
    /**
     * 保留量增量
     */
    private Long reserveInv;
    /**
     * 处理异常信息
     */
    private String errorMsg;

    public void check() {
        Assert.hasLength(transactionType, "missing match [transactionType]");
        Assert.hasLength(transactionSource, "missing match [transactionSource]");
        Assert.hasLength(skuCode, "missing match [skuCode]");
        Assert.hasLength(posCode, "missing match [posCode]");
        Assert.notNull(onHandInv, "missing match [onHandInv]");
        Assert.notNull(reserveInv, "missing match [reserveInv]");
    }
}
