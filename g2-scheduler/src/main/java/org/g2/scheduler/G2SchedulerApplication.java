package org.g2.scheduler;

import org.g2.autoconfigure.scheduler.EnableG2Scheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
