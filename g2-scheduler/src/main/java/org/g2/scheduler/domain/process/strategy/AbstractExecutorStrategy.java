package org.g2.scheduler.domain.process.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.g2.scheduler.domain.entity.Executor;
import org.g2.scheduler.domain.entity.ExecutorConfig;
import org.g2.scheduler.domain.service.IAddressService;
import org.g2.scheduler.infra.constants.SchedulerConstants;
import org.g2.starter.redis.client.RedisCacheClient;

/**
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
public abstract class AbstractExecutorStrategy implements ExecutorStrategy {


    protected static final int RETRY = 10;

    private final RedisCacheClient redisCacheClient;
    private final IAddressService addressService;

    protected AbstractExecutorStrategy(RedisCacheClient redisCacheClient, IAddressService iAddressService) {
        this.redisCacheClient = redisCacheClient;
        this.addressService = iAddressService;
    }

    protected List<String> usableUrl(Executor executor) {
        if (null == executor || Objects.equals(executor.getStatus(), SchedulerConstants.ExecutorStatue.OFFLINE)) {
            return new ArrayList<>();
        }
        return addressService.getServiceAddressList(executor);
    }

    protected boolean usable(String address, Long executorId,Long jobId) {
        List<String> cache = ExecutorConfig.getCache(redisCacheClient, executorId, address);
        if (cache.contains(String.valueOf(jobId))) {
            return true;
        } else {
            return ExecutorConfig.addExecutorCache(redisCacheClient, executorId, address);
        }
    }
}
