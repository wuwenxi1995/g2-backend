package org.g2.boot.inf.annotation;

import org.g2.boot.inf.infra.authenticate.Authenticator;
import org.g2.boot.inf.infra.authenticate.impl.NoneAuthenticator;
import org.springframework.http.HttpMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-16
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Call {

    HttpMethod method();

    String code();

    String contentType() default "application/x-www-form-urlencoded";

    Class<? extends Authenticator> authenticator() default NoneAuthenticator.class;
}
