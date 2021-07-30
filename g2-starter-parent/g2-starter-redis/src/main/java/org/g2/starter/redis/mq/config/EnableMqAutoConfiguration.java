package org.g2.starter.redis.mq.config;

import org.g2.starter.redis.mq.config.processor.MqBeanDefinitionRegisterProcessor;
import org.g2.starter.redis.mq.listener.config.ListenerProcessor;
import org.g2.starter.redis.mq.subject.config.SubjectProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
@Configuration
@Import(value = {MqBeanDefinitionRegisterProcessor.class, ListenerProcessor.class, SubjectProcessor.class})
public class EnableMqAutoConfiguration {
}
