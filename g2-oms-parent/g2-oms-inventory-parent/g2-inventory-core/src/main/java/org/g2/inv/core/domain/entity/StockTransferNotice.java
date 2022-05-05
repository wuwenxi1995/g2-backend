package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 库存调拨规则
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_transfer_notice")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockTransferNotice extends AuditDomain {

    @Id
    @GeneratedValue
    private Long stockTransferNoticeId;
}
