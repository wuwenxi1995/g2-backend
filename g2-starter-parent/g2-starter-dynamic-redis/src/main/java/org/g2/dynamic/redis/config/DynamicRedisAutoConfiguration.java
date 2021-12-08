package org.g2.dynamic.redis.config;

import org.g2.dynamic.redis.CustomizerRedisTemplateFactory;
import org.g2.dynamic.redis.config.properties.DynamicRedisProperties;
import org.g2.dynamic.redis.hepler.RedisHelper;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisTemplate;
import org.g2.dynamic.redis.hepler.sharding.ShardingRedisHelper;
import org.g2.dynamic.redis.hepler.sharding.ShardingRedisTemplate;
import org.g2.dynamic.redis.util.DatabaseThreadLocal;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.List;

/**
 * @author wuwenxi 2021-12-07
 */
@Configuration
@EnableConfigurationProperties({RedisProperties.class, DynamicRedisProperties.class})
@ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
@ComponentScan(basePackages = {"org.g2.dynamic.redis"})
public class DynamicRedisAutoConfiguration {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisSerializer serializer = new StringRedisSerializer();
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setStringSerializer(serializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean(initMethod = "init")
    public CustomizerRedisTemplateFactory<String, String> customizerRedisTemplateFactory(RedisProperties redisProperties,
                                                                                         ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
                                                                                         ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
                                                                                         ObjectProvider<List<JedisClientConfigurationBuilderCustomizer>> jedisBuilderCustomizers,
                                                                                         ObjectProvider<List<LettuceClientConfigurationBuilderCustomizer>> lettuceBuilderCustomizers) {
        return new CustomizerRedisTemplateFactory<>(redisProperties, sentinelConfiguration, clusterConfiguration, jedisBuilderCustomizers, lettuceBuilderCustomizers);
    }

    @Bean
    @ConditionalOnProperty(prefix = "g2.redis", name = "dynamic-database", havingValue = "false")
    public RedisHelper redisHelper() {
        return new RedisHelper();
    }

    @Bean
    @ConditionalOnProperty(prefix = "g2.redis", name = "dynamic-database", havingValue = "true", matchIfMissing = true)
    public RedisHelper dynamicRedisHelper(RedisProperties redisProperties, CustomizerRedisTemplateFactory<String, String> redisTemplateFactory) {
        DynamicRedisTemplate<String, String> dynamicRedisTemplate = new DynamicRedisTemplate<>(redisTemplateFactory);
        // 创建默认的redisTemplate
        RedisTemplate<String, String> defaultRedisTemplate = dynamicRedisTemplate.createRedisTemplateOnMissing(redisProperties.getDatabase());
        dynamicRedisTemplate.setDefaultRedisTemplate(defaultRedisTemplate);
        // 设置template映射关系
        HashMap<Object, RedisTemplate<String, String>> map = new HashMap<>(16);
        // 保存默认库对应的redisTemplate
        map.put(redisProperties.getDatabase(), defaultRedisTemplate);
        dynamicRedisTemplate.setRedisTemplates(map);
        return new DynamicRedisHelper(dynamicRedisTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "g2.redis", name = "sharding", havingValue = "true", matchIfMissing = true)
    public RedisHelper shardingRedisHelper(RedisProperties redisProperties, CustomizerRedisTemplateFactory<String, String> redisTemplateFactory) {
        ShardingRedisTemplate<String, String> shardingRedisTemplate = new ShardingRedisTemplate<>(redisTemplateFactory);
        try {
            DatabaseThreadLocal.set(redisProperties.getDatabase());
            RedisTemplate<String, String> redisTemplate = shardingRedisTemplate.createRedisTemplateOnMissing(redisProperties.getDatabase());
            shardingRedisTemplate.setDefaultRedisTemplate(redisTemplate);
        } finally {
            DatabaseThreadLocal.clear();
        }
        // 设置template映射关系
        HashMap<Object, RedisTemplate<String, String>> map = new HashMap<>(16);
        shardingRedisTemplate.setRedisTemplates(map);
        return new ShardingRedisHelper(shardingRedisTemplate);
    }
}
