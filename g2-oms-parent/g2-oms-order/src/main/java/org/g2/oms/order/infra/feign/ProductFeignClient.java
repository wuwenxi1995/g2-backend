package org.g2.oms.order.infra.feign;

import org.g2.oms.order.infra.feign.fallback.ProductFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@FeignClient(value = "g2-oms-product", path = "/v1", fallbackFactory = ProductFeignClientFallback.class)
public interface ProductFeignClient {

    @GetMapping("/test")
    String get();
}
