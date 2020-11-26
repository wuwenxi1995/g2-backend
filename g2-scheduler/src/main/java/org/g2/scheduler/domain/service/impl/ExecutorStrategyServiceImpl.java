package org.g2.scheduler.domain.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.g2.scheduler.domain.process.strategy.ExecutorStrategy;
import org.g2.scheduler.domain.service.ExecutorStrategyService;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public class ExecutorStrategyServiceImpl implements ExecutorStrategyService {

    private Map<String, ExecutorStrategy> executorStrategyCache = new ConcurrentHashMap<>();

    @Override
    public ExecutorStrategy getExecutorStrategy(String strategyCode) {
        return executorStrategyCache.get(strategyCode);
    }

    @Override
    public void register(String strategyCode, ExecutorStrategy executorStrategy) {
        executorStrategyCache.put(strategyCode, executorStrategy);
    }
}
