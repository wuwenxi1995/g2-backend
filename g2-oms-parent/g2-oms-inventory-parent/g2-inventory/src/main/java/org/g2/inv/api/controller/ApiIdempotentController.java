package org.g2.inv.api.controller;

import org.g2.inv.api.dto.ApiResponse;
import org.g2.inv.app.service.ApiIdempotentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生成请求token
 *
 * @author wuwenxi 2022-04-15
 */
@RestController
@RequestMapping("/idempotent-token")
public class ApiIdempotentController {

    private final ApiIdempotentService apiIdempotentService;

    public ApiIdempotentController(ApiIdempotentService apiIdempotentService) {
        this.apiIdempotentService = apiIdempotentService;
    }

    @GetMapping
    public ResponseEntity<?> generatorToken() {
        return ResponseEntity.ok(ApiResponse.ok(apiIdempotentService.token()));
    }
}
