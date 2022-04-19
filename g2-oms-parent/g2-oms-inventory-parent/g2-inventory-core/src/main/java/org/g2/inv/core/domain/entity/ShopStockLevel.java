package org.g2.inv.core.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g2.starter.mybatis.entity.AuditDomain;

import javax.persistence.Table;

/**
 * 网店库存
 *
 * @author wuwenxi 2022-04-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "g2inv_shop_stock_level")
public class ShopStockLevel extends AuditDomain {
}
