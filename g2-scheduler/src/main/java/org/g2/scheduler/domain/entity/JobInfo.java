package org.g2.scheduler.domain.entity;

import java.util.Date;

/**
 * 任务信息，表g2_scheduler.gsdr_job_info
 *
 * @author wenxi.wu@hand-chian.com 2020-12-01
 */
public class JobInfo {

    public static final String FIELD_JOB_ID = "jobId";
    public static final String FIELD_JOB_CODE = "jobCode";
    public static final String FIELD_EXECUTOR_ID = "executorId";
    public static final String FIELD_MAX_CONCURRENT = "executorStrategy";
    public static final String FIELD_JOB_HANDLER = "jobHandler";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_JOB_PARAM = "jobParam";
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_END_DATE = "endDate";
    public static final String FIELD_CRON = "cron";
    public static final String FIELD_IS_CYCLE = "isCycle";

    /**
     * 表主键
     */
    private Long jobId;
    /**
     * 任务编码
     */
    private String jobCode;
    /**
     * 执行器编码
     */
    private Long executorId;
    /**
     * 执行器策略
     */
    private String executorStrategy;
    /**
     * 任务执行编码
     */
    private String jobHandler;
    /**
     * 任务描述
     */
    private String description;
    /**
     * 任务参数
     */
    private String jobParam;
    /**
     * 失败策略
     */
    private String failStrategy;
    /**
     * 开始执行时间
     */
    private Date startDate;
    /**
     * 结束执行时间
     */
    private Date endDate;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * 是否周期性进行
     */
    private Integer isCycle;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public String getExecutorStrategy() {
        return executorStrategy;
    }

    public void setExecutorStrategy(String executorStrategy) {
        this.executorStrategy = executorStrategy;
    }

    public String getJobHandler() {
        return jobHandler;
    }

    public void setJobHandler(String jobHandler) {
        this.jobHandler = jobHandler;
    }

    public String getJobParam() {
        return jobParam;
    }

    public void setJobParam(String jobParam) {
        this.jobParam = jobParam;
    }

    public Integer getIsCycle() {
        return isCycle;
    }

    public void setIsCycle(Integer isCycle) {
        this.isCycle = isCycle;
    }

    public String getFailStrategy() {
        return failStrategy;
    }

    public void setFailStrategy(String failStrategy) {
        this.failStrategy = failStrategy;
    }
}
