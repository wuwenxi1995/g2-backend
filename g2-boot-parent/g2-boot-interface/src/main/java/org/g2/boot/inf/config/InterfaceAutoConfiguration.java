package org.g2.boot.inf.config;

import org.g2.boot.inf.infra.authenticate.impl.BasicAuthenticator;
import org.g2.boot.inf.infra.authenticate.impl.NoneAuthenticator;
import org.g2.boot.inf.infra.executor.InfClientExecutor;
import org.g2.boot.inf.infra.resolver.InfClientClassResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-16
 */
@Configuration
public class InterfaceAutoConfiguration {

    @Bean
    public InfClientClassResolver infClientClassResolver() {
        return new InfClientClassResolver();
    }

    @Bean
    public InfClientExecutor infClientExecutor() {
        return new InfClientExecutor();
    }

    @Bean
    public BasicAuthenticator basicAuthenticator() {
        return new BasicAuthenticator();
    }

    @Bean
    public NoneAuthenticator noneAuthenticator() {
        return new NoneAuthenticator();
    }
}
