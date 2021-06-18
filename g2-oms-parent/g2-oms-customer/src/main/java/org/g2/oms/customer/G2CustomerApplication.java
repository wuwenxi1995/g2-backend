package org.g2.oms.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wenxi.wu@hand-chian.com 2020-11-23
 */
@SpringBootApplication
@EnableDiscoveryClient
public class G2CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2CustomerApplication.class, args);
    }
}
