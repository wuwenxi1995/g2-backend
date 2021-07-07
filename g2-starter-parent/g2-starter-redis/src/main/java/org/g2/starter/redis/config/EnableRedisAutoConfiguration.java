package org.g2.starter.redis.config;

import java.util.Collection;
import java.util.List;

import org.g2.core.exception.CommonException;
import org.g2.core.handler.MethodInvocationHandler;
import org.g2.core.handler.impl.ChainInvocationHandler;
import org.g2.core.helper.ApplicationContextHelper;
import org.g2.starter.redis.client.RedisCacheClient;
import org.g2.starter.redis.config.factory.EnableShardingConnectionFactory;
import org.g2.starter.redis.config.handler.RedisConfiguration;
import org.g2.starter.redis.config.handler.impl.StandaloneConfiguration;
import org.g2.starter.redis.config.properties.RedisCacheProperties;
import org.g2.starter.redis.config.properties.RedisShardingProperties;
import org.g2.starter.redis.infra.hepler.RedisHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 配置自动装配
 *
 * @author wenxi.wu@hand-china.com on 2020/4/11 17:50
 */
@Configuration
@ConditionalOnClass(RedisTemplate.class)
@EnableConfigurationProperties({RedisCacheProperties.class, RedisShardingProperties.class})
@Import({EnableShardingConnectionFactory.class})
@ComponentScan(basePackages = "org.g2.starter.redis")
public class EnableRedisAutoConfiguration {

    @Bean(value = RedisCacheClient.BEAN_NAME)
    public RedisCacheClient redisCacheClient() {
        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) new RedisClientAutoConfigureHandler().proceed();
        lettuceConnectionFactory.afterPropertiesSet();
        return new RedisCacheClient(lettuceConnectionFactory);
    }

    @Bean
    public RedisHelper redisHelper() {
        return new RedisHelper();
    }

    private static class RedisClientAutoConfigureHandler extends ChainInvocationHandler {

        @Override
        public Object proceed() {
            try {
                return super.proceed();
            } catch (Exception e) {
                throw new CommonException(e);
            }
        }

        @Override
        protected Object invoke() {
            throw new CommonException("No suitable handler found");
        }

        @Override
        protected Class<? extends MethodInvocationHandler> beanType() {
            return RedisConfiguration.class;
        }
    }

}
