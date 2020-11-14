package org.g2.scheduler.domain.process.strategy.impl;

import org.g2.scheduler.domain.process.strategy.AbstractExecutorStrategy;
import org.g2.scheduler.domain.service.IAddressService;
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
    public String execute(Long executorId) {
        return null;
    }

    @Override
    public Long strategyId() {
        return 3L;
    }
}
