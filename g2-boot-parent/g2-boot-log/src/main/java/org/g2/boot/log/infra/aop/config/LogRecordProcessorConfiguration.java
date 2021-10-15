package org.g2.boot.log.infra.aop.config;

import org.g2.boot.log.infra.aop.spring.processor.LogRecordAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuwenxi 2021-10-13
 */
@Configuration
public class LogRecordProcessorConfiguration {

    @Bean
    public LogRecordAnnotationBeanPostProcessor logRecordAdvisor() {
        return new LogRecordAnnotationBeanPostProcessor();
    }
}
