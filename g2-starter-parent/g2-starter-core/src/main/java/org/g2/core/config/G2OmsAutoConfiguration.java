package org.g2.core.config;

import org.g2.core.helper.ApplicationContextHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-china.com 2020-11-05
 */
@Configuration
public class G2OmsAutoConfiguration {

    @Bean
    public ApplicationContextHelper applicationContextHelper() {
        return new ApplicationContextHelper();
    }
}
