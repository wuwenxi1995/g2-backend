package org.g2.inv.console.app.service;

import org.g2.inv.core.api.dto.StockReportDTO;
import org.g2.inv.core.domain.entity.StockReport;

import java.util.List;

/**
 * @author wuwenxi 2022-04-24
 */
public interface StockReportService {
    /**
     * 查询库存上报列表
     *
     * @param stockReport 查询条件
     * @return 库存上报列表
     */
    List<StockReport> list(StockReportDTO stockReport);

    /**
     * 库存上报明细
     *
     * @param reportCode 上报编码
     * @return 上报明细
     */
    StockReport detail(String reportCode);

    /**
     * 创建库存上报明细
     *
     * @param stockReport 库存上报明细
     */
    void create(StockReport stockReport);

    /**
     * 提交库存上报信息
     *
     * @param reportCodes 上报编码
     */
    void submit(List<String> reportCodes);
}
