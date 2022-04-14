package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.PosStock;
import org.g2.inv.core.domain.repository.PosStockRepository;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author wuwenxi 2022-04-14
 */
@Repository
public class PosStockRepositoryImpl extends BaseRepositoryImpl<PosStock> implements PosStockRepository {
}
