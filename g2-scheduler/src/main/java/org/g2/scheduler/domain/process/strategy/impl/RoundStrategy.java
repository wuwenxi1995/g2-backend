package org.g2.scheduler.domain.process.strategy.impl;

import java.util.List;

import com.netflix.loadbalancer.ILoadBalancer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.g2.scheduler.domain.entity.Executor;
import org.g2.scheduler.domain.process.strategy.AbstractExecutorStrategy;
import org.g2.scheduler.domain.repositoty.ExecutorRepository;
import org.g2.scheduler.domain.service.IAddressService;
import org.g2.scheduler.infra.constants.SchedulerConstants;
import org.g2.scheduler.infra.exception.SchedulerException;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

/**
 * 轮询算法
 *
 * @author wenxi.wu@hand-china.com 2020-11-09
 */
@Component
public class RoundStrategy extends AbstractExecutorStrategy {

    private List<String> addressList;

    private final RedisCacheClient redisCacheClient;
    private final ExecutorRepository executorRepository;

    public RoundStrategy(RedisCacheClient redisCacheClient, IAddressService addressService, ExecutorRepository executorRepository) {
        super(redisCacheClient, addressService);
        this.redisCacheClient = redisCacheClient;
        this.executorRepository = executorRepository;
    }

    @Override
    public String execute(Long executorId, Long jobId) {
        Executor executor = executorRepository.selectByPrimaryKey(executorId);
        this.addressList = this.usableUrl(executor);
        if (CollectionUtils.isEmpty(addressList)) {
            return "";
        }

        String address = "";
        int count = 0;
        while (StringUtils.isBlank(address) && count++ < RETRY) {
            // 获取执行器列表下标
            Integer index = incrementAndGetModulo(executorId, jobId);
            address = addressList.get(index);
            // 判断执行器是否可用
            if (usable(address, executorId, jobId)) {
                break;
            }
        }

        if (count >= RETRY) {
            throw new SchedulerException("No available alive servers after [%s] tries from load balancer", RETRY);
        }

        return address;
    }

    /**
     * 自旋锁
     *
     * @see com.netflix.loadbalancer.RoundRobinRule#choose(ILoadBalancer, Object)
     */
    private Integer incrementAndGetModulo(Long executorId, Long jobId) {
        int current, next;
        while (true) {
            current = getCache(executorId, jobId);
            if (current == Integer.MAX_VALUE) {
                next = 0;
            } else {
                next = (current + 1) % addressList.size();
            }
            if (refreshCache(executorId, jobId, current, next)) {
                return next;
            }
        }
    }

    private Boolean refreshCache(Long executorId, Long jobId, Integer current, Integer next) {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        script.setScriptSource(SchedulerConstants.Cache.RedisScript.ROUND_INDEX);
        script.setResultType(Boolean.class);
        String key = getKey(executorId, jobId);
        return redisCacheClient.execute(script, null, key, current, next);
    }

    private Integer getCache(Long executorId, Long jobId) {
        String key = getKey(executorId, jobId);
        String value = redisCacheClient.opsForValue().get(key);
        return StringUtils.isBlank(value) ? -1 : Integer.parseInt(value);
    }

    private String getKey(Long executorId, Long jobId) {
        return String.format(SchedulerConstants.Cache.Key.ROUND_INDEX, executorId, jobId);
    }

    @Override
    public String strategyCode() {
        return SchedulerConstants.ExecutorStrategy.ROUND;
    }
}
