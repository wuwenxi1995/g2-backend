package org.g2.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author wenxi.wu@hand-china.com 2020-10-14
 */
@SpringBootApplication
@EnableEurekaServer
public class G2EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2EurekaApplication.class, args);
    }
}
