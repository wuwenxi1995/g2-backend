package org.g2.scheduler;

import org.g2.autoconfigura.scheduler.annotation.EnableG2Scheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wenxi.wu@hand-china.com 2020-11-02
 */
@EnableG2Scheduler
@SpringBootApplication
@EnableDiscoveryClient
public class G2SchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(G2SchedulerApplication.class, args);
    }
}
