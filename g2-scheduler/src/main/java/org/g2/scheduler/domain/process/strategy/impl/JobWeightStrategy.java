package org.g2.scheduler.domain.process.strategy.impl;

import org.g2.scheduler.domain.process.strategy.AbstractExecutorStrategy;
import org.g2.scheduler.domain.service.IAddressService;
import org.g2.scheduler.infra.constants.SchedulerConstants;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.stereotype.Component;

/**
 * 任务权限算法
 *
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Component
public class JobWeightStrategy extends AbstractExecutorStrategy {

    private final RedisCacheClient redisCacheClient;

    public JobWeightStrategy(RedisCacheClient redisCacheClient, IAddressService addressService) {
        super(redisCacheClient, addressService);
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public String execute(Long executorId, Long jobId) {
        return null;
    }

    @Override
    public String strategyCode() {
        return SchedulerConstants.ExecutorStrategy.JOB_WEIGHT;
    }
}
