package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.StockReport;
import org.g2.inv.core.domain.repository.StockReportRepository;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author wuwenxi 2022-04-22
 */
@Repository
public class StockReportRepositoryImpl extends BaseRepositoryImpl<StockReport> implements StockReportRepository {
}
