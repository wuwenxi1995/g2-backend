package org.g2.starter.mq.config;

import org.g2.starter.mq.subject.publish.Publisher;
import org.g2.starter.redis.client.RedisCacheClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-18
 */
@Configuration
public class EnableMqAutoConfiguration {

    @Bean
    public Publisher publisher(RedisCacheClient redisCacheClient) {
        return new Publisher(redisCacheClient);
    }
}
