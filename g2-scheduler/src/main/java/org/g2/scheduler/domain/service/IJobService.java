package org.g2.scheduler.domain.service;

import org.g2.scheduler.domain.entity.JobInfo;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-01
 */
public interface IJobService {

    /**
     * 添加任务
     *
     * @param jobInfo 任务信息
     */
    void addJob(JobInfo jobInfo);

    /**
     * 更新任务
     *
     * @param jobInfo 任务信息
     */
    void updateJob(JobInfo jobInfo);

    /**
     * 移除任务
     *
     * @param jobId      任务id
     * @param executorId 执行器id
     */
    void removeJob(Long jobId, Long executorId);

    /**
     * 触发任务
     *
     * @param jobId 任务id
     */
    void trigger(Long jobId);

    /**
     * 暂停任务
     *
     * @param jobId 任务id
     */
    void pauseJob(Long jobId);

    /**
     * 恢复任务
     *
     * @param jobId 任务id
     */
    void resumeJob(Long jobId);
}
