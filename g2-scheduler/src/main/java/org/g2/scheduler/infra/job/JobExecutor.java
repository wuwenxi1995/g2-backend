package org.g2.scheduler.infra.job;

import org.apache.commons.lang3.StringUtils;
import org.g2.scheduler.domain.service.IUrlService;
import org.g2.core.helper.ApplicationContextHelper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author wenxi.wu@hand-china.com 2020-11-03
 */
public class JobExecutor implements Job {

    private IUrlService urlService = ApplicationContextHelper.getApplicationContext().getBean(IUrlService.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        long executorId = jobDataMap.getLongValue("executorId");
        long jobId = jobDataMap.getLongValue("jobId");

        String executorStrategy = jobDataMap.getString("executorStrategy");

        String address = urlService.getServiceUrl(executorStrategy, executorId, jobId);
        if (StringUtils.isBlank(address)) {

        }
    }
}
