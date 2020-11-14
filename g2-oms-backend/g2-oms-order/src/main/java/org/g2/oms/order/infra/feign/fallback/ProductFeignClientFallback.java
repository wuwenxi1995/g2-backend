package org.g2.oms.order.infra.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.g2.oms.order.infra.feign.ProductFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@Component
public class ProductFeignClientFallback implements FallbackFactory<ProductFeignClient> {

    @Override
    public ProductFeignClient create(Throwable throwable) {
        return new ProductFeignClient() {
            @Override
            public String get() {
                return "product has errror";
            }
        };
    }
}
