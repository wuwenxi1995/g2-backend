package org.g2.inv.core.infra.repository.impl;

import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.g2.inv.core.infra.mapper.InvTransactionMapper;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wuwenxi 2022-04-22
 */
@Repository
public class InvTransactionRepositoryImpl extends BaseRepositoryImpl<InvTransaction> implements InvTransactionRepository {

    @Autowired
    private InvTransactionMapper transactionMapper;

    @Override
    public List<InvTransaction> findByCodes(List<String> transactionCodes) {
        return null;
    }
}
