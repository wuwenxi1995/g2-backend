package org.g2.autoconfigure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wenxi.wu@hand-china.com 2020-11-13
 */
@Configuration
@EnableAsync
@EnableConfigServer
@EnableConfigurationProperties
public class ConfigAutoConfiguration {
}
