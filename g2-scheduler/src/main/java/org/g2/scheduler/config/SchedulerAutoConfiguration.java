package org.g2.scheduler.config;

import java.util.List;

import org.g2.scheduler.domain.process.strategy.ExecutorStrategy;
import org.g2.scheduler.domain.repositoty.ExecutorRepository;
import org.g2.scheduler.domain.service.ExecutorStrategyService;
import org.g2.scheduler.domain.service.impl.ExecutorStrategyServiceImpl;
import org.g2.scheduler.infra.init.ExecutorServiceInitJob;
import org.g2.scheduler.infra.repository.impl.ExecutorRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Configuration
public class SchedulerAutoConfiguration {

    @Bean
    public ExecutorServiceInitJob executorServiceInitJob() {
        return new ExecutorServiceInitJob();
    }

    @Bean
    public ExecutorStrategyService executorStrategyService(List<ExecutorStrategy> executorStrategies) {
        ExecutorStrategyServiceImpl executorStrategyService = new ExecutorStrategyServiceImpl();
        if (CollectionUtils.isEmpty(executorStrategies)) {
            return executorStrategyService;
        }
        for (ExecutorStrategy executorStrategy : executorStrategies) {
            executorStrategyService.register(executorStrategy.strategyId(), executorStrategy);
        }
        return executorStrategyService;
    }

    @Bean
    public ExecutorRepository executorRepository() {
        return new ExecutorRepositoryImpl();
    }
}

