package org.g2.oms.order.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.g2.oms.order.app.service.OrderService;
import org.g2.oms.order.infra.feign.ProductFeignClient;
import org.springframework.stereotype.Service;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ProductFeignClient productFeignClient;

    public OrderServiceImpl(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    @Override
    public String test() {
        long start = System.currentTimeMillis();
        log.info("request ...");
        String result = productFeignClient.get();
        log.info("return , times:{}", System.currentTimeMillis() - start);
        return result;
    }
}
