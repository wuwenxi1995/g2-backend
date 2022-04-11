package org.g2.inv.calculate.app.task;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.g2.core.CoreConstants;
import org.g2.core.task.TaskHandler;
import org.g2.core.thread.scheduler.ScheduledTask;
import org.g2.core.util.ThreadFactoryBuilder;
import org.g2.dynamic.redis.hepler.RedisHelper;
import org.g2.inv.calculate.app.handler.transaction.TransactionHandlerChain;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.inv.core.domain.repository.InvTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wuwenxi 2022-04-11
 */
@Component
public class InvTransactionTaskHandler extends TaskHandler {

    private static final Logger log = LoggerFactory.getLogger(InvTransactionTaskHandler.class);

    private ScheduledThreadPoolExecutor scheduled;

    private final RedisHelper redisHelper;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final InvTransactionRepository invTransactionRepository;
    private final TransactionHandlerChain transactionHandlerChain;

    public InvTransactionTaskHandler(@Qualifier("dynamicRedisHelper") RedisHelper redisHelper,
                                     @Qualifier("invThreadPool") ThreadPoolTaskExecutor taskExecutor,
                                     InvTransactionRepository invTransactionRepository,
                                     TransactionHandlerChain transactionHandlerChain) {
        this.redisHelper = redisHelper;
        this.taskExecutor = taskExecutor;
        this.invTransactionRepository = invTransactionRepository;
        this.transactionHandlerChain = transactionHandlerChain;
    }

    @Override
    protected void run() {
        ScheduledTask task = new ScheduledTask("transactionTask", scheduled, taskExecutor.getThreadPoolExecutor(), new TransactionTask(), 1, TimeUnit.MINUTES, false);
        // 立即执行任务
        this.scheduled.execute(task);
    }

    @Override
    protected void doStart() {
        this.scheduled = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setDaemon(true).build());
    }

    @Override
    protected void doStop() {
        scheduled.shutdown();
    }

    private class TransactionTask implements Runnable {

        @Override
        public void run() {
            redisHelper.setCurrentDataBase(0);
            try {
                while (isRunning()) {
                    String transactionCode = redisHelper.lstRightPop(InvCalculateConstants.RedisKey.INVENTORY_TRANSACTION_KEY);
                    if (StringUtils.isBlank(transactionCode)) {
                        return;
                    }
                    InvTransaction invTransaction = new InvTransaction();
                    invTransaction.setTransactionCode(transactionCode);
                    invTransaction.setProcessingStatusCode(CoreConstants.ProcessStatus.PENDING);
                    List<InvTransaction> transactions = invTransactionRepository.select(invTransaction);
                    if (CollectionUtils.isNotEmpty(transactions)) {
                        // 将库存事务按照门店和事务类型分组处理
                        Map<InvTransaction, List<InvTransaction>> groupByPosCode = transactions.stream().collect(Collectors.groupingBy(e -> new InvTransaction(e.getPosCode(), e.getTransactionType())));
                        groupByPosCode.forEach((key, value) -> {
                            try {
                                transactionHandlerChain.proceed(key.getTransactionType(), key.getPosCode(), value);
                            } catch (Exception e) {
                                log.error("库存事务处理异常,posCode:{},transactionType:{}", key.getPosCode(), key.getTransactionType());
                            }
                        });
                    }
                }
            } finally {
                redisHelper.clearCurrentDataBase();
            }
        }
    }
}
