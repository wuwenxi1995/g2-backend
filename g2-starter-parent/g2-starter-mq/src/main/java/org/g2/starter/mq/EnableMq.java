package org.g2.starter.mq;

import org.g2.starter.mq.spring.register.MqProcessorRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Import(value = MqProcessorRegister.class)
public @interface EnableMq {

    String[] basePackages();
}
