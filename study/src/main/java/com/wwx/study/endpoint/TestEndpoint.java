package com.wwx.study.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

/**
 * @author wuwenxi 2022-01-07
 */
@Component
@Endpoint(id = "test")
public class TestEndpoint {

    private static final Logger log = LoggerFactory.getLogger(TestEndpoint.class);

    @ReadOperation
    public String get() {
        log.info(" ========== read ============== ");
        return "success";
    }

    @ReadOperation
    public String put(@Selector String name) {
        log.info("============= put name : {} =========== ", name);
        return name;
    }

    @WriteOperation
    public void write(String message) {
        log.info(" ========= write msg : {} ======= ", message);
    }
}
