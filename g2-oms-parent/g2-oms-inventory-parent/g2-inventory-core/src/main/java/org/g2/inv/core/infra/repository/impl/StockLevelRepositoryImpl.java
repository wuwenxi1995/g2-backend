package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author wuwenxi 2022-04-22
 */
@Repository
public class StockLevelRepositoryImpl extends BaseRepositoryImpl<StockLevel> implements StockLevelRepository {
}
