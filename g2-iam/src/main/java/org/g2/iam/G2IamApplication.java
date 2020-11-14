package org.g2.iam;

import org.g2.autoconfigure.iam.EnableG2Iam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wenxi.wu@hand-china.com 2020-11-13
 */
@EnableG2Iam
@SpringBootApplication
@EnableDiscoveryClient
public class G2IamApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2IamApplication.class, args);
    }
}
