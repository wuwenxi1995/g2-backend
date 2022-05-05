package org.g2.inv.core.domain.repository;

import org.g2.inv.core.api.dto.StockReportDTO;
import org.g2.inv.core.domain.entity.StockReport;
import org.g2.starter.mybatis.base.BaseRepository;

import java.util.List;

/**
 * @author wuwenxi 2022-04-22
 */
public interface StockReportRepository extends BaseRepository<StockReport> {

    /**
     * 查询库存上报信息
     *
     * @param stockReport 查询信息
     * @return 库存上报信息
     */
    List<StockReport> list(StockReportDTO stockReport);

    /**
     * 根据上报编码查询创建状态的上报明细
     *
     * @param reportCodes 上报编码
     * @return 库存上报信息
     */
    List<StockReport> selectCreateReportEntry(List<String> reportCodes);
}
