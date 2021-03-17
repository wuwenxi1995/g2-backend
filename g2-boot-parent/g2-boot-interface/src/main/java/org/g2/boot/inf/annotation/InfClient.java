package org.g2.boot.inf.annotation;

import org.springframework.stereotype.Component;

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
@Component
public @interface InfClient {

    /**
     * 是否创建代理对象
     *
     * @return 默认创建
     */
    boolean createProxy() default true;
}
