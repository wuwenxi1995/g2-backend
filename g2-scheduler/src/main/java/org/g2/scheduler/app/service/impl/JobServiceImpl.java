package org.g2.scheduler.app.service.impl;

import org.g2.scheduler.infra.job.JobExecutor;
import org.g2.scheduler.app.service.JobService;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

/**
 * @author wenxi.wu@hand-china.com 2020-11-03
 */
@Service
public class JobServiceImpl implements JobService {

    @Override
    public void createJob() {
        try {
            SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
            Scheduler scheduler = factoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob(JobExecutor.class)
                    .withDescription("")
                    .withIdentity("", "")
                    .usingJobData(getJobDataMap())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private JobDataMap getJobDataMap() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobHandler", "jobHandler");
        return jobDataMap;
    }
}
