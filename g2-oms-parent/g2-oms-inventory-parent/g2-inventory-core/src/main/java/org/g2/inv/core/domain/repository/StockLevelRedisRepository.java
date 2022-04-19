package org.g2.inv.core.domain.repository;

import org.g2.inv.core.domain.entity.StockLevel;

/**
 * @author wuwenxi 2022-04-13
 */
public interface StockLevelRedisRepository extends StockRedisRepository {

    /**
     * 库存同步缓存
     *
     * @param stockLevel 库存信息
     */
    void sync(StockLevel stockLevel);
}
