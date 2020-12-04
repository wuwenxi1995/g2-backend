package org.g2.scheduler.app.service.impl;

import org.g2.scheduler.app.service.JobInfoService;
import org.g2.scheduler.domain.entity.JobInfo;
import org.g2.scheduler.domain.repositoty.JobInfoRepository;
import org.g2.scheduler.domain.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wenxi.wu@hand-china.com 2020-11-03
 */
@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private IJobService jobService;

    @Autowired
    private JobInfoRepository jobInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JobInfo createJob(JobInfo jobInfo) {
        jobInfoRepository.insertSelective(jobInfo);
        jobService.addJob(jobInfo);
        return jobInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JobInfo updateJob(JobInfo jobInfo) {
        jobInfoRepository.updateByPrimaryKeySelective(jobInfo);
        jobService.updateJob(jobInfo);
        return jobInfo;
    }

    @Override
    public void removeJob(List<JobInfo> jobInfoList) {

    }

    @Override
    public void stopJob(JobInfo jobInfo) {
    }
}
