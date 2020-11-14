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
        long strategy;
        switch (executorStrategy) {
            case SchedulerConstants.ExecutorStrategy.ROUND:
                strategy = 0L;
                break;
            case SchedulerConstants.ExecutorStrategy.RANDOM:
                strategy = 1L;
                break;
            case SchedulerConstants.ExecutorStrategy.WEIGHT:
                strategy = 2L;
                break;
            case SchedulerConstants.ExecutorStrategy.JOB_WEIGHT:
                strategy = 3L;
                break;
            default:
                strategy = -1L;
                break;
        }

        if (strategy == -1) {
            return "";
        }

        ExecutorStrategy run = executorStrategyService.getExecutorStrategy(strategy);
        if (null != run) {
            return run.execute(executorId, jobId);
        }
        return "";
    }
}
