package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Service;

/**
 * @author wuwenxi 2022-04-09
 */
@Service
public class InvTransactionRepositoryImpl extends BaseRepositoryImpl<InvTransaction> implements InvTransactionRepository {
}
