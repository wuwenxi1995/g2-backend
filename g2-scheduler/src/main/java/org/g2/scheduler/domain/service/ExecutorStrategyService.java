package org.g2.scheduler.domain.service;

import org.g2.scheduler.domain.process.strategy.ExecutorStrategy;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public interface ExecutorStrategyService {

    /**
     * 获取执行器策略
     *
     * @param strategyCode 策略id
     * @return 执行器策略
     */
    ExecutorStrategy getExecutorStrategy(String strategyCode);

    /**
     * 缓存执行器策略
     *
     * @param strategyCode       策略id
     * @param executorStrategy 执行器策略
     */
    void register(String strategyCode, ExecutorStrategy executorStrategy);
}
