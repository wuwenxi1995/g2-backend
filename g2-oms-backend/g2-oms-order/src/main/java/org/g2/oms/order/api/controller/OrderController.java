package org.g2.oms.order.api.controller;

import org.g2.core.util.Results;
import org.g2.oms.order.app.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
