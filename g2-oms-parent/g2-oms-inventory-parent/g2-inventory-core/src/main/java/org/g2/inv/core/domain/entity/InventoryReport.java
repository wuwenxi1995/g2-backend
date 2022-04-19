package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.Table;

/**
 * 库存上报
 *
 * @author wuwenxi 2022-04-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "g2inv_report")
public class InventoryReport extends AuditDomain {


}
