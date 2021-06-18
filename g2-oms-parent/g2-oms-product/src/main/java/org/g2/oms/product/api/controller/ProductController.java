package org.g2.oms.product.api.controller;

import org.g2.core.util.Results;
import org.g2.oms.product.app.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@RestController
@RequestMapping("/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return Results.success(productService.get());
    }

    @GetMapping("/publish")
    public ResponseEntity<?> publish() {
        productService.publish();
        return Results.success();
    }

    @GetMapping("/listener")
    public ResponseEntity<?> listener(){
        productService.listener();
        return Results.success();
    }
}
