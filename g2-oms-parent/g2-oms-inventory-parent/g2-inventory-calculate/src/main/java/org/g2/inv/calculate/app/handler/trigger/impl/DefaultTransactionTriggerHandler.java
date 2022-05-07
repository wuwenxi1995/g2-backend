package org.g2.inv.calculate.app.handler.trigger.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.g2.inv.calculate.app.handler.transaction.TransactionHandlerChain;
import org.g2.inv.calculate.app.handler.trigger.TransactionTriggerHandler;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.g2.inv.core.domain.vo.TriggerMessage;
import org.g2.inv.core.infra.constant.InvCoreConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-05-07
 */
@Component
public class DefaultTransactionTriggerHandler implements TransactionTriggerHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultTransactionTriggerHandler.class);

    private final InvTransactionRepository transactionRepository;
    private final TransactionHandlerChain transactionHandlerChain;

    public DefaultTransactionTriggerHandler(InvTransactionRepository transactionRepository, TransactionHandlerChain transactionHandlerChain) {
        this.transactionRepository = transactionRepository;
        this.transactionHandlerChain = transactionHandlerChain;
    }

    @Override
    public void handler(List<TriggerMessage> triggerMessages) {
        List<String> transactionCodes = triggerMessages.stream().map(e -> (String) e.getContent()).collect(Collectors.toList());
        List<InvTransaction> transactions = this.transactionRepository.findByCodes(transactionCodes);
        if (CollectionUtils.isNotEmpty(transactions)) {
            // 将库存事务按照门店和事务类型分组处理
            Map<String, Map<String, List<InvTransaction>>> groupMap = transactions.stream().collect(Collectors.groupingBy(InvTransaction::getTransactionType, HashMap::new, Collectors.groupingBy(InvTransaction::getPosCode)));
            long start = System.currentTimeMillis();
            transactionHandlerChain.proceed(groupMap);
            log.info("库存事务处理完成, 事务编码:{}, 处理时间:{}", transactionCodes.toString(), (System.currentTimeMillis() - start));

        }
    }

    @Override
    public String type() {
        return InvCoreConstants.TriggerType.TRANSACTION_TRIGGER;
    }
}
