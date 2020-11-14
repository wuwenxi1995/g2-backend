package org.g2.scheduler.domain.process.strategy.impl;

import org.g2.scheduler.domain.process.strategy.AbstractExecutorStrategy;
import org.g2.scheduler.domain.service.IAddressService;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.stereotype.Component;

/**
 * 执行器权重算法
 *
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Component
public class WeightStrategy extends AbstractExecutorStrategy {

    private final RedisCacheClient redisCacheClient;

    public WeightStrategy(RedisCacheClient redisCacheClient, IAddressService addressService) {
        super(redisCacheClient, addressService);
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public String execute(Long executorId) {
        return null;
    }

    @Override
    public Long strategyId() {
        return 2L;
    }
}
