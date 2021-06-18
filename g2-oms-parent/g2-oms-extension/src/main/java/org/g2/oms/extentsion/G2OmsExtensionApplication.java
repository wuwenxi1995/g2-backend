package org.g2.oms.extentsion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wenxi.wu@hand-chian.com 2020-11-24
 */
@SpringBootApplication
@EnableDiscoveryClient
public class G2OmsExtensionApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2OmsExtensionApplication.class, args);
    }
}
