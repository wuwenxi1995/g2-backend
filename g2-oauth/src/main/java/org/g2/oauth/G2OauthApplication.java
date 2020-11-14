package org.g2.oauth;

import org.g2.autoconfigure.oauth.EnableG2Oauth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wenxi.wu@hand-china.com 2020-11-13
 */
@EnableG2Oauth
@SpringBootApplication
@EnableDiscoveryClient
public class G2OauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2OauthApplication.class, args);
    }
}
