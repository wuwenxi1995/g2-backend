package org.g2.scheduler.domain.repositoty;

import org.g2.scheduler.domain.entity.Executor;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public interface ExecutorRepository {
    /**
     * 按照主键查询执行器
     *
     * @param executorId 执行器id
     * @return 执行器
     */
    Executor selectByPrimaryKey(Long executorId);
}
