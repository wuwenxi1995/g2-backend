package org.g2.inv.infra.feign.fallback;

import org.g2.inv.infra.feign.InvConsoleFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author wuwenxi 2022-04-09
 */
public class InvConsoleFeignClientFallback implements FallbackFactory<InvConsoleFeignClient> {

    @Override
    public InvConsoleFeignClient create(Throwable cause) {
        return null;
    }
}
