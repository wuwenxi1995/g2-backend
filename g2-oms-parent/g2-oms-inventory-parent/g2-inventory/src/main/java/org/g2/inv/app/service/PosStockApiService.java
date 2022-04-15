package org.g2.inv.app.service;

import org.g2.inv.api.dto.pos.PosStockOperateDTO;
import org.g2.inv.api.dto.pos.PosStockQueryDTO;
import org.g2.inv.core.domain.vo.StockQueryResponseVO;

import java.util.List;

/**
 * @author wuwenxi 2022-04-15
 */
public interface PosStockApiService {
    /**
     * 查询服务点库存
     *
     * @param posStockQueryDTO 查询条件
     * @return 查询结果
     */
    List<StockQueryResponseVO> query(PosStockQueryDTO posStockQueryDTO);

    /**
     * 库存占用
     *
     * @param posStockOperateDTO 请求内容
     * @return true/false
     */
    boolean occupy(PosStockOperateDTO posStockOperateDTO);

    /**
     * 库存释放
     *
     * @param posStockOperateDTO 请求内容
     * @return true/false
     */
    boolean release(PosStockOperateDTO posStockOperateDTO);

    /**
     * 库存扣减
     *
     * @param posStockOperateDTO 请求内容
     * @return true/false
     */
    boolean reduce(PosStockOperateDTO posStockOperateDTO);
}
