package org.g2.oms.order.api.controller;

import org.g2.core.util.Results;
import org.g2.core.util.ThreadPoolExecutorUtil;
import org.g2.oms.order.app.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@RestController
@RequestMapping("/v1")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return Results.success(orderService.test());
    }

    @GetMapping("/test01")
    public ResponseEntity<?> testThread(@RequestParam Long size) {
        orderService.test02(size);
        return Results.success();
    }
}
