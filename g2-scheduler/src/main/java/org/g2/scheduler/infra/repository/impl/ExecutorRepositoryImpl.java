package org.g2.scheduler.infra.repository.impl;

import org.g2.scheduler.domain.entity.Executor;
import org.g2.scheduler.domain.repositoty.ExecutorRepository;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Component
public class ExecutorRepositoryImpl extends BaseRepositoryImpl<Executor> implements ExecutorRepository {
}
