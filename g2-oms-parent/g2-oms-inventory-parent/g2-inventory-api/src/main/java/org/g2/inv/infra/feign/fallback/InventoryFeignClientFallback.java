package org.g2.inv.infra.feign.fallback;

import org.g2.inv.infra.feign.InventoryFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author wuwenxi 2022-04-09
 */
public class InventoryFeignClientFallback implements FallbackFactory<InventoryFeignClient> {

    @Override
    public InventoryFeignClient create(Throwable cause) {
        return null;
    }
}
