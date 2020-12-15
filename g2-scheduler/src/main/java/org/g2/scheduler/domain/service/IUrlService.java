package org.g2.scheduler.domain.service;

import org.g2.scheduler.domain.process.strategy.ExecutorStrategy;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public interface IUrlService {

    /**
     * 获取服务ip地址
     *
     * @param executorStrategy 执行器策略
     * @param executorId       执行器id
     * @param jobId            定时任务id
     * @return ip
     */
    String getServiceUrl(String executorStrategy, Long executorId, Long jobId);

    /**
     * 获取服务ip地址
     *
     * @param executorId 执行器id
     * @param jobId      任务id
     * @return 服务地址
     */
    String getServiceUrl(Long executorId, Long jobId);

    /**
     * 获取服务ip地址
     *
     * @param executorStrategy 执行器策略
     * @param executorId       执行器id
     * @param jobId            任务id
     * @return 服务地址
     */
    String getServiceUrl(ExecutorStrategy executorStrategy, Long executorId, Long jobId);
}
