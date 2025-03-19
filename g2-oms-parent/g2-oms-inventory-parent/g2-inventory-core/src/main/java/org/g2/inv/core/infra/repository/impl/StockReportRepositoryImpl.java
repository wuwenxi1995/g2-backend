package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.api.dto.StockReportDTO;
import org.g2.inv.core.domain.entity.StockReport;
import org.g2.inv.core.domain.repository.StockReportRepository;
import org.g2.inv.core.infra.mapper.StockReportMapper;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wuwenxi 2022-04-22
 */
@Repository
public class StockReportRepositoryImpl extends BaseRepositoryImpl<StockReport> implements StockReportRepository {

    private final StockReportMapper stockReportMapper;

    public StockReportRepositoryImpl(StockReportMapper stockReportMapper) {
        this.stockReportMapper = stockReportMapper;
    }

    @Override
    public List<StockReport> list(StockReportDTO stockReport) {
        return stockReportMapper.list(stockReport);
    }

    @Override
    public List<StockReport> selectCreateReportEntry(List<String> reportCodes) {
        return null;
    }
}
