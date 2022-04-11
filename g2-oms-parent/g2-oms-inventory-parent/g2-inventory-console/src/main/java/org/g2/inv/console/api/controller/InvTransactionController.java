package org.g2.inv.console.api.controller;

import org.g2.inv.console.app.service.InvTransactionService;
import org.g2.inv.core.domain.entity.InvTransaction;
import org.g2.starter.mybatis.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wuwenxi 2022-04-09
 */
@RestController
@RequestMapping("/inv-transaction")
public class InvTransactionController {

    @Autowired
    private InvTransactionService invTransactionService;

    @GetMapping("/list")
    public ResponseEntity<?> list(InvTransaction invTransaction, PageRequest pageRequest) {
        return ResponseEntity.ok(invTransactionService.list(invTransaction));
    }

    @GetMapping
    public ResponseEntity<?> detail(@RequestParam Long invTransactionId) {
        return ResponseEntity.ok(invTransactionService.detail(invTransactionId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody List<InvTransaction> invTransactions) {
        invTransactionService.create(invTransactions);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody List<InvTransaction> invTransactions) {
        invTransactionService.upload(invTransactions);
        return ResponseEntity.ok().build();
    }
}
