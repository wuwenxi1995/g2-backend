package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.StockTransfer;
import org.g2.inv.core.domain.repository.StockTransferRepository;
import org.hzero.mybatis.base.BaseRepository;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author wuwenxi 2022-04-24
 */
@Repository
public class StockTransferRepositoryImpl extends BaseRepositoryImpl<StockTransfer> implements StockTransferRepository {
}
