package org.g2.oms.order.infra.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.g2.oms.order.infra.feign.ProductFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@Component
public class ProductFeignClientFallback implements FallbackFactory<ProductFeignClient> {
    private static final Logger log = LoggerFactory.getLogger(ProductFeignClientFallback.class);
    private static final String COMMON_ERROR_MSG = "can not get response, cause by:";

    @Override
    public ProductFeignClient create(Throwable throwable) {
        return new ProductFeignClient() {
            @Override
            public String get() {
                log.error(COMMON_ERROR_MSG + throwable);
                return null;
            }
        };
    }
}
