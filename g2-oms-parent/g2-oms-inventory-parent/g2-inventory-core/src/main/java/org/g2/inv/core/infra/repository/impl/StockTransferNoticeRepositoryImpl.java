package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.StockTransferNotice;
import org.g2.inv.core.domain.repository.StockTransferNoticeRepository;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author wuwenxi 2022-04-24
 */
@Repository
public class StockTransferNoticeRepositoryImpl extends BaseRepositoryImpl<StockTransferNotice> implements StockTransferNoticeRepository {
}
