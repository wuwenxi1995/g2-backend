package org.g2.micservice.inv.console;

import org.g2.core.util.StringUtil;
import org.g2.inv.console.config.EnableInvConsole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wuwenxi 2022-05-07
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableInvConsole
public class InvConsoleApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(InvConsoleApplication.class, args);
        } catch (Exception e) {
            System.out.println(StringUtil.exceptionString(e));
        }
    }
}
