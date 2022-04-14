package org.g2.inv.calculate.app.trigger.pos;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.g2.core.task.TaskHandler;
import org.g2.core.thread.scheduler.ScheduledTask;
import org.g2.core.util.ThreadFactoryBuilder;
import org.g2.dynamic.redis.hepler.RedisHelper;
import org.g2.inv.calculate.infra.constant.InvCalculateConstants;
import org.g2.inv.trigger.domain.vo.TransactionTriggerVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwenxi 2022-04-13
 */
@Component
public class PosStockTsTriggerTaskHandler extends TaskHandler {

    private ScheduledThreadPoolExecutor scheduled;

    private final ThreadPoolTaskExecutor taskExecutor;
    private final RedisHelper redisHelper;

    public PosStockTsTriggerTaskHandler(@Qualifier("invThreadPool") ThreadPoolTaskExecutor taskExecutor,
                                        @Qualifier("dynamicRedisHelper") RedisHelper redisHelper) {
        this.taskExecutor = taskExecutor;
        this.redisHelper = redisHelper;
    }

    private void doTask() {
        redisHelper.setCurrentDataBase(0);
        try {
            while (isRunning()) {
                String tsTriggers = redisHelper.lstLeftPop(InvCalculateConstants.RedisKey.TRANSACTION_TRIGGER_POS_KEY);
                if (StringUtils.isBlank(tsTriggers)) {
                    return;
                }
                List<TransactionTriggerVO> triggers = JSONObject.parseArray(tsTriggers, TransactionTriggerVO.class);
            }
        } finally {
            redisHelper.clearCurrentDataBase();
        }
    }

    @Override
    protected void run() {
        ScheduledTask scheduledTask = new ScheduledTask("posStockTsTriggerTaskHandler", scheduled, taskExecutor.getThreadPoolExecutor(), this::doTask, 1, TimeUnit.MINUTES, false);
        // 立即启动
        this.scheduled.schedule(scheduledTask, 0, TimeUnit.NANOSECONDS);
    }

    @Override
    protected void doStart() {
        this.scheduled = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setDaemon(true).build());
    }

    @Override
    protected void doStop() {
        if (!scheduled.isShutdown()) {
            scheduled.shutdown();
        }
    }
}
