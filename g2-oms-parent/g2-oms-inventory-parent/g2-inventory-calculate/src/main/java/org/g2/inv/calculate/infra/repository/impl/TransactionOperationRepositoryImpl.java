package org.g2.inv.calculate.infra.repository.impl;

import org.g2.inv.calculate.domain.repository.StockLevelRedisRepository;
import org.g2.inv.calculate.domain.repository.TransactionOperationRepository;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.entity.StockLevel;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.g2.inv.core.domain.repository.StockLevelRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuwenxi 2022-04-12
 */
@Repository
public class TransactionOperationRepositoryImpl implements TransactionOperationRepository {

    private final StockLevelRepository stockLevelRepository;
    private final InvTransactionRepository invTransactionRepository;
    private final StockLevelRedisRepository stockLevelRedisRepository;

    public TransactionOperationRepositoryImpl(StockLevelRepository stockLevelRepository, InvTransactionRepository invTransactionRepository, StockLevelRedisRepository stockLevelRedisRepository) {
        this.stockLevelRepository = stockLevelRepository;
        this.invTransactionRepository = invTransactionRepository;
        this.stockLevelRedisRepository = stockLevelRedisRepository;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Override
    public void persistence(StockLevel stockLevel, InvTransaction invTransaction, boolean isCreate) {
        if (isCreate) {
            stockLevelRepository.insertSelective(stockLevel);
        } else {
            stockLevelRepository.updateByPrimaryKey(stockLevel);
        }
        invTransactionRepository.updateOptional(invTransaction, InvTransaction.FILED_PROCESSING_STATUS_CODE);
        // 库存同步缓存
        stockLevelRedisRepository.sync(stockLevel);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Override
    public void persistence(StockLevel stockLevel, List<InvTransaction> invTransactions, boolean isCreate) {
        if (isCreate) {
            stockLevelRepository.insertSelective(stockLevel);
        } else {
            stockLevelRepository.updateByPrimaryKey(stockLevel);
        }
        invTransactionRepository.batchUpdateOptional(invTransactions, InvTransaction.FILED_PROCESSING_STATUS_CODE);
        // 库存同步缓存
        stockLevelRedisRepository.sync(stockLevel);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Override
    public void updateOperational(List<InvTransaction> invTransactions, String... fields) {
        invTransactionRepository.batchUpdateOptional(invTransactions, fields);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Override
    public void updateOperational(InvTransaction invTransaction, String... fields) {
        invTransactionRepository.updateOptional(invTransaction, fields);
    }
}
