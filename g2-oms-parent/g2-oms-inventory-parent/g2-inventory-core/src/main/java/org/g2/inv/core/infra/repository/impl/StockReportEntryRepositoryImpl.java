package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.StockReportEntry;
import org.g2.inv.core.domain.repository.StockReportEntryRepository;
import org.g2.inv.core.infra.mapper.StockReportEntryMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author wuwenxi 2022-04-22
 */
@Repository
public class StockReportEntryRepositoryImpl extends BaseRepositoryImpl<StockReportEntry> implements StockReportEntryRepository {

    private final StockReportEntryMapper stockReportEntryMapper;

    public StockReportEntryRepositoryImpl(StockReportEntryMapper stockReportEntryMapper) {
        this.stockReportEntryMapper = stockReportEntryMapper;
    }
}
