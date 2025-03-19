package org.g2.oms.micservice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wuwenxi 2022-04-08
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.g2.oms.micservice")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
