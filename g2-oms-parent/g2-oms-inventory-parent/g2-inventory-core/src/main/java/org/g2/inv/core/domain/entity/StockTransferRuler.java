package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.Table;

/**
 * 库存调拨规则
 *
 * @author wuwenxi 2022-04-22
 */
@Table(name = "inv_stock_transfer_rule")
@EqualsAndHashCode(callSuper = true)
@Data
public class StockTransferRuler extends AuditDomain {
}
