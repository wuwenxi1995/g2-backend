package org.g2.config;

import org.g2.autoconfigure.config.EnableG2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wenxi.wu@hand-china.com 2020-11-13
 */
@EnableG2Config
@SpringBootApplication
@EnableDiscoveryClient
public class G2ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2ConfigApplication.class, args);
    }
}
