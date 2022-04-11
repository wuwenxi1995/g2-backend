package org.g2.inv.infra.feign;

import org.g2.inv.infra.feign.fallback.InventoryFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wuwenxi 2022-04-09
 */
@FeignClient(name = "g2-Inventory", fallbackFactory = InventoryFeignClientFallback.class)
public interface InventoryFeignClient {
}
