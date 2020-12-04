package org.g2.scheduler.app.service;

import org.g2.scheduler.domain.entity.JobInfo;

import java.util.List;

/**
 * @author wenxi.wu@hand-china.com 2020-11-03
 */
public interface JobInfoService {

    /**
     * 创建任务
     *
     * @param jobInfo 任务信息
     * @return 返回信息
     */
    JobInfo createJob(JobInfo jobInfo);

    /**
     * 更新任务
     *
     * @param jobInfo 任务信息
     * @return 返回信息
     */
    JobInfo updateJob(JobInfo jobInfo);

    /**
     * 删除任务
     *
     * @param jobInfoList 任务
     */
    void removeJob(List<JobInfo> jobInfoList);

    /**
     * 终止任务
     *
     * @param jobInfo 结束任务
     */
    void stopJob(JobInfo jobInfo);
}
