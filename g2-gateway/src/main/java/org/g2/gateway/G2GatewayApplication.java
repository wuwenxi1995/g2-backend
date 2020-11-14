package org.g2.gateway;

import org.g2.autoconfigure.gateway.EnableG2Gateway;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wenxi.wu@hand-china.com 2020-11-11
 */
@EnableG2Gateway
@SpringBootApplication
@EnableDiscoveryClient
public class G2GatewayApplication {

    public static void main(String[] args) {
        try {
            new SpringApplicationBuilder(G2GatewayApplication.class)
                    .web(WebApplicationType.REACTIVE)
                    .run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
