package org.g2.boot.scheduler.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenxi.wu@hand-china.com 2020-11-03
 */
@RestController
@RequestMapping("/v1/scheduler")
public class JobExecuteController {

    @RequestMapping(value = "/executor")
    public ResponseEntity<?> executor() {
        return null;
    }
}
