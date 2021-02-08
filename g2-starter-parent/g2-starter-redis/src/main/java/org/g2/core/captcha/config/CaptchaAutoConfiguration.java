package org.g2.core.captcha.config;

import org.g2.core.captcha.CaptchaMessageHandler;
import org.g2.core.captcha.config.properties.CaptchaProperties;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-04
 */
@Configuration
@EnableConfigurationProperties({CaptchaProperties.class})
public class CaptchaAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedisCacheClient.class)
    public CaptchaMessageHandler captchaMessageHandler(CaptchaProperties captchaProperties,
                                                       RedisCacheClient redisCacheClient) {
        return new CaptchaMessageHandler(captchaProperties, redisCacheClient);
    }
}
