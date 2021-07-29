package org.g2.starter.aop.annotation;

import org.g2.starter.aop.config.spring.register.InterceptorRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wuwenxi 2021-07-29
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Import(InterceptorRegister.class)
public @interface EnableInterceptor {

    /**
     * 扫描路径
     */
    String[] basePackages();
}
