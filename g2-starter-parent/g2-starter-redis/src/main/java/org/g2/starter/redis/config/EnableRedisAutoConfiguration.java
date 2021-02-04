package org.g2.starter.redis.config;

import java.util.Objects;

import org.g2.starter.redis.client.RedisCacheClient;
import org.g2.starter.redis.config.factory.EnableLettuceConnectionFactory;
import org.g2.starter.redis.config.factory.EnableShardingConnectionFactory;
import org.g2.starter.redis.config.properties.RedisCacheProperties;
import org.g2.starter.redis.config.properties.RedisProperties;
import org.g2.starter.redis.config.properties.RedisShardingProperties;
import org.g2.starter.redis.infra.hepler.RedisHelper;
import org.g2.starter.redis.infra.queue.IQueue;
import org.g2.starter.redis.infra.queue.impl.DefaultQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * redis 配置自动装配
 *
 * @author wenxi.wu@hand-china.com on 2020/4/11 17:50
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
@EnableConfigurationProperties({RedisCacheProperties.class, RedisShardingProperties.class})
@Import({EnableLettuceConnectionFactory.class, EnableShardingConnectionFactory.class})
public class EnableRedisAutoConfiguration {

    private final LettuceConnectionFactory lettuceConnectionFactory;
    private final RedisCacheClient redisCacheClient;

    public EnableRedisAutoConfiguration(@Qualifier("customizerLettuceConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory, RedisCacheClient redisCacheClient) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
        this.redisCacheClient = redisCacheClient;
    }

    @Bean
    public RedisCacheClient redisCacheClient() {
        return new RedisCacheClient(lettuceConnectionFactory);
    }

    @Bean
    public IQueue defaultQueue() {
        return new DefaultQueue(this.redisCacheClient());
    }

    @Bean
    public RedisHelper redisHelper() {
        return new RedisHelper();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(Objects.requireNonNull(redisCacheClient.getConnectionFactory()));
        return redisMessageListenerContainer;
    }

}
