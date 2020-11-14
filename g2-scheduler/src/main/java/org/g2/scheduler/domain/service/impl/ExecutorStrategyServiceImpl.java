package org.g2.scheduler.domain.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.g2.scheduler.domain.process.strategy.ExecutorStrategy;
import org.g2.scheduler.domain.service.ExecutorStrategyService;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public class ExecutorStrategyServiceImpl implements ExecutorStrategyService {

    private Map<Long, ExecutorStrategy> cache = new ConcurrentHashMap<>();

    @Override
    public ExecutorStrategy getExecutorStrategy(Long strategyId) {
        return cache.get(strategyId);
    }

    @Override
    public void register(Long strategyId, ExecutorStrategy executorStrategy) {
        cache.put(strategyId, executorStrategy);
    }
}
