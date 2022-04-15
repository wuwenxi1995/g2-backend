package org.g2.inv.core.domain.repository;

import org.g2.inv.core.domain.vo.StockQueryResponseVO;

import java.util.List;
import java.util.Map;

/**
 * @author wuwenxi 2022-04-15
 */
public interface PosStockRedisRepository extends StockRedisRepository {

    /**
     * 查询服务点库存信息
     *
     * @param posCode 服务点编码
     * @param skuList sku
     * @return 库存信息
     */
    List<StockQueryResponseVO> readPosStock(String posCode, Map<String, Long> skuList);

    /**
     * 库存占用
     *
     * @param posCode 服务点编码
     * @param items   sku
     * @return true/false
     */
    boolean occupy(String posCode, Map<String, Long> items);

    /**
     * 库存释放
     *
     * @param posCode 服务点编码
     * @param items   sku
     * @return true/false
     */
    boolean release(String posCode, Map<String, Long> items);

    /**
     * 库存扣减
     *
     * @param posCode 服务点编码
     * @param items   sku
     * @return true/false
     */
    boolean reduce(String posCode, Map<String, Long> items);
}
