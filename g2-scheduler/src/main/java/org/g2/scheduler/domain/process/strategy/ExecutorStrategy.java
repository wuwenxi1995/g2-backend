package org.g2.scheduler.domain.process.strategy;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public interface ExecutorStrategy {

    /**
     * 执行算法
     *
     * @param executorId 执行器id
     * @param jobId      任务id
     * @return ip
     */
    String execute(Long executorId, Long jobId);

    /**
     * 设置策略id
     *
     * @return strategyId
     */
    Long strategyId();
}
