package org.g2.inv.console.app.service.impl;

import org.g2.core.CoreConstants;
import org.g2.inv.console.app.service.InvTransactionService;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.g2.inv.trigger.app.service.TriggerInvTransactionService;
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
    private final TriggerInvTransactionService triggerInvTransactionService;

    public InvTransactionServiceImpl(InvTransactionRepository invTransactionRepository, TriggerInvTransactionService triggerInvTransactionService) {
        this.invTransactionRepository = invTransactionRepository;
        this.triggerInvTransactionService = triggerInvTransactionService;
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
        triggerInvTransactionService.triggerCalculate(invTransactionCode);
    }

    @Override
    public void upload(List<InvTransaction> invTransactions) {

    }

    private String invTransactionCode() {
        return "";
    }
}
