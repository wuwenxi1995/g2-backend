package org.g2.core.feign;

import feign.Logger.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 打印feign日志
 *
 * @author wenxi.wu@hand-china.com 2020-11-12
 */
@Configuration
public class FeignLogger {

    @Bean
    public Level feignLoggerLevel() {
        return Level.FULL;
    }
}
