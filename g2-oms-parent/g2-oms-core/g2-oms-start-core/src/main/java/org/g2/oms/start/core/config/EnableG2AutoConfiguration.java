package org.g2.oms.start.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wenxi.wu@hand-chian.com 2020-12-03
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = false)
@EnableAsync
@EnableConfigurationProperties
public class EnableG2AutoConfiguration {
}
