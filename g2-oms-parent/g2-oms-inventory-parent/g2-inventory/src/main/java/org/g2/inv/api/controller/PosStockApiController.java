package org.g2.inv.api.controller;

import org.g2.inv.api.dto.ApiIdempotentRequest;
import org.g2.inv.api.dto.pos.PosStockOperateDTO;
import org.g2.inv.api.dto.pos.PosStockQueryDTO;
import org.g2.inv.app.service.ApiIdempotentService;
import org.g2.inv.app.service.PosStockApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务点库存查询
 *
 * @author wuwenxi 2022-04-15
 */
@RestController
@RequestMapping("/pos-stock-api")
public class PosStockApiController {

    private final PosStockApiService posStockApiService;
    private final ApiIdempotentService apiIdempotentService;

    public PosStockApiController(PosStockApiService posStockApiService, ApiIdempotentService apiIdempotentService) {
        this.posStockApiService = posStockApiService;
        this.apiIdempotentService = apiIdempotentService;
    }

    @PostMapping("/query")
    public ResponseEntity<?> query(@RequestBody PosStockQueryDTO posStockQueryDTO) {
        return ResponseEntity.ok(posStockApiService.query(posStockQueryDTO));
    }

    @PostMapping("/occupy")
    public ResponseEntity<?> occupy(@RequestBody ApiIdempotentRequest<PosStockOperateDTO> request) {
        return ResponseEntity.ok(apiIdempotentService.idempotentOperation(request.getToken(), () -> posStockApiService.occupy(request.getContent())));
    }

    @PostMapping("/release")
    public ResponseEntity<?> release(@RequestBody ApiIdempotentRequest<PosStockOperateDTO> request) {
        return ResponseEntity.ok(apiIdempotentService.idempotentOperation(request.getToken(), () -> posStockApiService.release(request.getContent())));
    }

    public ResponseEntity<?> reduce(@RequestBody ApiIdempotentRequest<PosStockOperateDTO> request) {
        return ResponseEntity.ok(apiIdempotentService.idempotentOperation(request.getToken(), () -> posStockApiService.reduce(request.getContent())));
    }
}
