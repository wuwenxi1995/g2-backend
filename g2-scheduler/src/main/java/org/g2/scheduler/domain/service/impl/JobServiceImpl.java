package org.g2.scheduler.domain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.g2.scheduler.domain.entity.JobInfo;
import org.g2.scheduler.domain.service.IJobService;
import org.g2.scheduler.infra.constants.SchedulerConstants;
import org.g2.scheduler.infra.exception.SchedulerException;
import org.g2.scheduler.infra.job.JobExecutor;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-01
 */
@Service
public class JobServiceImpl implements IJobService {

    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private static final String DEFAULT = "0";

    @Autowired
    private Scheduler scheduler;

    @Override
    public void addJob(JobInfo jobInfo) {
        // 添加执行器缓存

        // 添加任务
        try {
            JobDetail jobDetail = JobBuilder.newJob(JobExecutor.class)
                    .withDescription(jobInfo.getDescription())
                    .withIdentity(String.valueOf(jobInfo.getJobId()), DEFAULT)
                    .usingJobData(getJobDataMap(jobInfo))
                    .build();
            Trigger trigger = buildTrigger(jobInfo);
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
            log.info(" ------------------ add job success , jobId :{} ----------------------- ", jobInfo.getJobId());
        } catch (Exception e) {
            throw new SchedulerException("add job error ", e);
        }
    }

    @Override
    public void updateJob(JobInfo jobInfo) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(jobInfo.getJobId()), DEFAULT);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger != null) {
                JobKey jobKey = JobKey.jobKey(String.valueOf(jobInfo.getJobId()), DEFAULT);
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);
                if (!scheduler.checkExists(jobKey)) {
                    throw new SchedulerException("");
                }
                addJob(jobInfo);
            }
        } catch (Exception e) {
            throw new SchedulerException("update job error", e);
        }
    }

    @Override
    public void removeJob(Long jobId, Long executorId) {
        // 删除执行器缓存

        // 删除任务
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(jobId), DEFAULT);
            JobKey jobKey = JobKey.jobKey(String.valueOf(jobId), DEFAULT);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            log.info(" ------------------ remove job success , jobId :{} ----------------------- ", jobId);
        } catch (org.quartz.SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void trigger(Long jobId) {
        try {
            scheduler.triggerJob(JobKey.jobKey(String.valueOf(jobId), DEFAULT));
        } catch (Exception e) {
            throw new SchedulerException("trigger job error", e);
        }
    }

    @Override
    public void pauseJob(Long jobId) {
        try {
            scheduler.pauseJob(JobKey.jobKey(String.valueOf(jobId), DEFAULT));
        } catch (Exception e) {
            throw new SchedulerException("pause job error", e);
        }
    }

    @Override
    public void resumeJob(Long jobId) {
        try {
            scheduler.resumeJob(JobKey.jobKey(String.valueOf(jobId), DEFAULT));
        } catch (Exception e) {
            throw new SchedulerException("resume job error", e);
        }
    }

    @Override
    public String getJobStatus(Long jobId) {
        try {
            TriggerKey triggerKey = new TriggerKey(String.valueOf(jobId), DEFAULT);
            return scheduler.getTriggerState(triggerKey).name();
        } catch (Exception e) {
            throw new SchedulerException("查询任务状态发生错误:%s", e, e.getMessage());
        }
    }

    private Trigger buildTrigger(JobInfo jobInfo) {
        TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(jobInfo.getJobId()), DEFAULT);
        Trigger trigger;
        if (Objects.equals(jobInfo.getIsCycle(), 1)) {
            if (StringUtils.isBlank(jobInfo.getCron())) {
                // 不执行任务
                jobInfo.setCron("0 0 0 1 * ? 2100");
            }

            CronScheduleBuilder cronScheduler = CronScheduleBuilder.cronSchedule(jobInfo.getCron()).withMisfireHandlingInstructionDoNothing();
            TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduler);
            if (jobInfo.getStartDate() != null && jobInfo.getEndDate() != null) {
                trigger = triggerBuilder.startAt(jobInfo.getStartDate()).endAt(jobInfo.getEndDate()).build();
            } else if (jobInfo.getStartDate() != null && jobInfo.getEndDate() == null) {
                trigger = triggerBuilder.startAt(jobInfo.getStartDate()).build();
            } else {
                triggerBuilder.startAt(new Date(System.currentTimeMillis() + 10000));
                if (jobInfo.getEndDate() != null) {
                    triggerBuilder.endAt(jobInfo.getEndDate());
                }
                trigger = triggerBuilder.build();
            }
        } else if (jobInfo.getStartDate() == null) {
            // 立刻生效，不重复执行
            trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0)).startNow().build();
        } else {
            // 指定时间后生效，不重复执行
            trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0)).startAt(jobInfo.getStartDate()).build();
        }
        return trigger;
    }

    private JobDataMap getJobDataMap(JobInfo jobInfo) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobCode", jobInfo.getJobCode());
        jobDataMap.put("executorId", jobInfo.getJobId());
        jobDataMap.put("executorStrategy", jobInfo.getExecutorStrategy());
        jobDataMap.put("jobHandler", jobInfo.getJobHandler());
        jobDataMap.put("param", jobInfo.getJobParam());
        jobDataMap.put("failStrategy", jobInfo.getFailStrategy());
        return jobDataMap;
    }
}
