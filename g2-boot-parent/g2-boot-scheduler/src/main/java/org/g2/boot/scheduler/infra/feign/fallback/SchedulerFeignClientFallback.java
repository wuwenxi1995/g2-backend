package org.g2.boot.scheduler.infra.feign.fallback;

import org.g2.boot.scheduler.infra.feign.SchedulerFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@Component
public class SchedulerFeignClientFallback implements FallbackFactory<SchedulerFeignClient> {

    @Override
    public SchedulerFeignClient create(Throwable throwable) {
        return (executorCode, serverName) -> null;
    }
}
