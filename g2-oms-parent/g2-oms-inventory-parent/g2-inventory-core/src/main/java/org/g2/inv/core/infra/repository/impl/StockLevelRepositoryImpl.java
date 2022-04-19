package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Service;

/**
 * @author wuwenxi 2022-04-12
 */
@Service
public class StockLevelRepositoryImpl extends BaseRepositoryImpl<StockLevel> implements StockLevelRepository {
}
