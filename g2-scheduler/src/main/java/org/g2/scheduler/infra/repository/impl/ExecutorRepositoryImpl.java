package org.g2.scheduler.infra.repository.impl;

import org.g2.scheduler.domain.entity.Executor;
import org.g2.scheduler.domain.repositoty.ExecutorRepository;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public class ExecutorRepositoryImpl implements ExecutorRepository {

    @Override
    public Executor selectByPrimaryKey(Long executorId) {
        return null;
    }
}
