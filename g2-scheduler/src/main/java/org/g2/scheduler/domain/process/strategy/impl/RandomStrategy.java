package org.g2.scheduler.domain.process.strategy.impl;

import java.util.List;
import java.util.Random;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.g2.scheduler.domain.entity.Executor;
import org.g2.scheduler.domain.process.strategy.AbstractExecutorStrategy;
import org.g2.scheduler.domain.repositoty.ExecutorRepository;
import org.g2.scheduler.domain.service.IAddressService;
import org.g2.scheduler.infra.exception.SchedulerException;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.stereotype.Component;

/**
 * 随机算法
 *
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Component
public class RandomStrategy extends AbstractExecutorStrategy {

    private final ExecutorRepository executorRepository;

    protected RandomStrategy(RedisCacheClient redisCacheClient, IAddressService addressService, ExecutorRepository executorRepository) {
        super(redisCacheClient, addressService);
        this.executorRepository = executorRepository;
    }

    @Override
    public String execute(Long executorId, Long jobId) {
        Executor executor = executorRepository.selectByPrimaryKey(executorId);
        List<String> addressList = this.usableUrl(executor);
        if (CollectionUtils.isEmpty(addressList)) {
            return "";
        }

        String address = "";
        int count = 0;
        Random random = new Random(addressList.size());
        while (StringUtils.isBlank(address) && count++ < RETRY) {
            int index = random.nextInt();
            address = addressList.get(index);
            if (usable(address, executorId, jobId)) {
                break;
            }
        }

        if (count >= RETRY) {
            throw new SchedulerException("No available alive servers after 10 tries from load balancer");
        }

        return address;
    }

    @Override
    public Long strategyId() {
        return 1L;
    }
}
