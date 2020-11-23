package org.g2.oms.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wenxi.wu@hand-china.com 2020-10-14
 */
@EnableFeignClients({"org.g2.oms.product"})
@ComponentScan(basePackages = {"org.g2.oms"})
@SpringBootApplication
@EnableEurekaClient
public class G2OmsProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2OmsProductApplication.class, args);
    }
}
