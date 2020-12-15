package org.g2.scheduler.domain.service.impl;

import org.g2.scheduler.domain.process.strategy.ExecutorStrategy;
import org.g2.scheduler.domain.service.ExecutorStrategyService;
import org.g2.scheduler.domain.service.IUrlService;
import org.g2.scheduler.infra.constants.SchedulerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Service
public class UrlServiceImpl implements IUrlService {

    private static final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);

    private final ExecutorStrategyService executorStrategyService;

    public UrlServiceImpl(ExecutorStrategyService executorStrategyService) {
        this.executorStrategyService = executorStrategyService;
    }

    @Override
    public String getServiceUrl(String executorStrategy, Long executorId, Long jobId) {
        ExecutorStrategy run = executorStrategyService.getExecutorStrategy(executorStrategy);
        if (null != run) {
            return run.execute(executorId, jobId);
        }
        return "";
    }

    @Override
    public String getServiceUrl(Long executorId, Long jobId) {
        // 默认使用轮询算法
        ExecutorStrategy executorStrategy = executorStrategyService.getExecutorStrategy(SchedulerConstants.ExecutorStrategy.ROUND);
        return executorStrategy.execute(executorId, jobId);
    }

    @Override
    public String getServiceUrl(ExecutorStrategy executorStrategy, Long executorId, Long jobId) {
        return executorStrategy.execute(executorId, jobId);
    }
}
