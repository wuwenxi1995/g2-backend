package org.g2.starter.aop.config;

import org.g2.starter.aop.config.processor.InterceptorRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wuwenxi 2021-07-29
 */
@Configuration
@Import(value = {InterceptorRegistryPostProcessor.class})
public class InterceptorConfiguration {
}
