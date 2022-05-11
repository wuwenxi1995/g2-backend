package org.g2.inv.console.api.controller;

import org.g2.core.util.Result;
import org.g2.core.util.Results;
import org.g2.inv.console.app.service.StockReportService;
import org.g2.inv.core.api.dto.StockReportDTO;
import org.g2.inv.core.domain.entity.StockReport;
import org.g2.starter.mybatis.page.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wuwenxi 2022-04-24
 */
@RestController
@RequestMapping("/stock-report")
public class StockReportController {

    private final StockReportService stockReportService;

    public StockReportController(StockReportService stockReportService) {
        this.stockReportService = stockReportService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(StockReportDTO stockReport, PageRequest pageRequest) {
        return Results.success();
    }

    @GetMapping
    public ResponseEntity<?> detail(@RequestParam String reportCode) {
        return Results.success(stockReportService.detail(reportCode));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StockReport stockReport) {
        Result<String> result = new Result<>(true, 200);
        try {
            stockReportService.create(stockReport);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(500);
            result.setMsg(e.getMessage());
        }
        return Results.success(result);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit(@RequestBody List<String> reportCodes) {
        stockReportService.submit(reportCodes);
        return Results.success();
    }
}
