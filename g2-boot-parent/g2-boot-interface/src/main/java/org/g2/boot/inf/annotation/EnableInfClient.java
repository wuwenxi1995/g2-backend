package org.g2.boot.inf.annotation;

import org.g2.boot.inf.config.spring.InfClientBeanProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-16
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Import({InfClientBeanProcessor.class})
public @interface EnableInfClient {
}
