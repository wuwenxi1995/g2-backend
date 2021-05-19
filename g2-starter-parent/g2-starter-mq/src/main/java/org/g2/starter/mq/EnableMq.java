package org.g2.starter.mq;

import org.g2.starter.mq.listener.annotation.Listener;
import org.g2.starter.mq.config.spring.register.MqProcessorRegister;
import org.g2.starter.mq.subject.annotation.Subject;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 开启基于注解的redis发布订阅 {@link Subject}及redis监听 {@link Listener}
 *
 * @author wenxi.wu@hand-chian.com 2021-05-17
 * </p>
 * @see org.g2.starter.mq.config.spring.register.MqProcessorRegister
 * @see org.g2.starter.mq.config.spring.processor.MqBeanDefinitionRegisterProcessor
 * @see org.g2.starter.mq.config.spring.scan.ClassPathMqScanner
 * @see org.g2.starter.mq.listener.config.ListenerProcessor
 * @see org.g2.starter.mq.subject.config.SubjectProcessor
 * @see org.g2.starter.mq.subject.publish.Publisher
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Import(value = MqProcessorRegister.class)
public @interface EnableMq {

    String[] basePackages();
}
