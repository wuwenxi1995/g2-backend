package org.g2.boot.scheduler.infra.feign;

import org.g2.boot.scheduler.infra.feign.fallback.SchedulerFeignClientFallback;
import org.g2.starter.common.G2Service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wenxi.wu@hand-china.com 2020-11-10
 */
@FeignClient(value = G2Service.G2Scheduler.NAME, path = "/v1", fallbackFactory = SchedulerFeignClientFallback.class)
public interface SchedulerFeignClient {

    /**
     * 刷新执行器状态
     *
     * @param executorCode 执行器编码
     * @param serverName   服务名
     * @return 刷新结果
     */
    @PostMapping("/executor/refresh")
    ResponseEntity<String> refreshExecutor(@RequestParam("executorCode") String executorCode,
                                           @RequestParam("serverName") String serverName);
}
