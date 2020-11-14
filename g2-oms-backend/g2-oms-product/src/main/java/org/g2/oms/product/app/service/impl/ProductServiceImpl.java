package org.g2.oms.product.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.g2.oms.product.app.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Override
    public String get() {
        log.info("request ... ");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("return ...");
        return "get from product";
    }
}
