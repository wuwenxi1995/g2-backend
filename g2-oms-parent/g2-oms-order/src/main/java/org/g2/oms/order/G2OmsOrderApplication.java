package org.g2.oms.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@EnableFeignClients({"org.g2.oms.order"})
@ComponentScan(basePackages = {"org.g2.oms"})
@SpringBootApplication
@EnableEurekaClient
public class G2OmsOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2OmsOrderApplication.class, args);
    }
}
