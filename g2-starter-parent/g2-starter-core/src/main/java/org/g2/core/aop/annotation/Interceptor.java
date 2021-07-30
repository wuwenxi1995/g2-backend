package org.g2.core.aop.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解只适用在{@link MethodInterceptor}实现类上
 *
 * @author wuwenxi 2021-07-29
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Interceptor {

    /**
     * 拦截class对象
     *
     * @return class
     */
    Class<?> clazz();

    /**
     * 拦截执行方法
     *
     * @return method
     */
    String[] method();
}
