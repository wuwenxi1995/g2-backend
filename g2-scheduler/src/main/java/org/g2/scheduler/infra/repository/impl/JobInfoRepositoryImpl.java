package org.g2.scheduler.infra.repository.impl;

import org.g2.scheduler.domain.entity.JobInfo;
import org.g2.scheduler.domain.repositoty.JobInfoRepository;
import org.g2.starter.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-01
 */
@Component
public class JobInfoRepositoryImpl extends BaseRepositoryImpl<JobInfo> implements JobInfoRepository {
}
