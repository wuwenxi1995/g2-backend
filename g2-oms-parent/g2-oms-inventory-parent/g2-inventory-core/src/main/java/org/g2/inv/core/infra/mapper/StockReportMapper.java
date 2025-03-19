package org.g2.inv.core.infra.mapper;

import org.g2.inv.core.api.dto.StockReportDTO;
import org.g2.inv.core.domain.entity.StockReport;
import org.g2.starter.mybatis.common.BaseMapper;

import java.util.List;

/**
 * @author wuwenxi 2022-04-22
 */
public interface StockReportMapper extends BaseMapper<StockReport> {

    /**
     * 查询库存上报列表信息
     *
     * @param stockReport 查询信息
     * @return 库存上报列表
     */
    List<StockReport> list(StockReportDTO stockReport);
}
