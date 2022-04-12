package org.g2.inv.console.app.service.impl;

import org.g2.core.CoreConstants;
import org.g2.dynamic.redis.hepler.RedisHelper;
import org.g2.dynamic.redis.safe.SafeRedisHelper;
import org.g2.inv.console.app.service.InvTransactionService;
import org.g2.inv.console.infra.InventoryConsoleConstant;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author wuwenxi 2022-04-09
 */
@Service
public class InvTransactionServiceImpl implements InvTransactionService {

    private final InvTransactionRepository invTransactionRepository;

    private final RedisHelper redisHelper;

    private final SafeRedisHelper safeRedisHelper;

    public InvTransactionServiceImpl(InvTransactionRepository invTransactionRepository, @Qualifier(value = "dynamicRedisHelper") RedisHelper redisHelper, SafeRedisHelper safeRedisHelper) {
        this.invTransactionRepository = invTransactionRepository;
        this.redisHelper = redisHelper;
        this.safeRedisHelper = safeRedisHelper;
    }

    @Override
    public List<InvTransaction> list(InvTransaction invTransaction) {
        return invTransactionRepository.select(invTransaction);
    }

    @Override
    public InvTransaction detail(Long invTransactionId) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(List<InvTransaction> invTransactions) {
        String invTransactionCode = invTransactionCode();
        for (InvTransaction invTransaction : invTransactions) {
            invTransaction.setTransactionCode(invTransactionCode);
            invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.PENDING);
            invTransaction.setSourceDate(new Date());
        }
        this.invTransactionRepository.batchInsertSelective(invTransactions);
        safeRedisHelper.execute(0, redisHelper, () -> {
            redisHelper.lstLeftPush(InventoryConsoleConstant.RedisKey.INVENTORY_TRANSACTION_KEY, invTransactionCode);
        });
    }

    @Override
    public void upload(List<InvTransaction> invTransactions) {

    }

    private String invTransactionCode() {
        return "";
    }
}
