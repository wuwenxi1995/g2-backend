package org.g2.message;

import org.g2.autoconfigure.message.EnableG2Message;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
@EnableG2Message
@SpringBootApplication
@EnableDiscoveryClient
public class G2MessageApplication {
}
