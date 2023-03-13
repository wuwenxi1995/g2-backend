package org.g2.dynamic.jdbc.config;

import com.zaxxer.hikari.HikariDataSource;
import org.g2.dynamic.jdbc.DynamicRoutDataSource;
import org.g2.dynamic.jdbc.DynamicRoutResolver;
import org.g2.dynamic.jdbc.aop.aspect.DataRoutInterceptor;
import org.g2.dynamic.jdbc.config.properties.DynamicJdbcProperties;
import org.g2.dynamic.jdbc.factory.MemoryDataSourceKeyFactory;
import org.g2.dynamic.jdbc.factory.RedisDatasourceFactory;
import org.g2.dynamic.jdbc.factory.DataSourceFactory;
import org.g2.dynamic.jdbc.factory.DataSourceFactoryWrapper;
import org.g2.dynamic.jdbc.factory.DataSourceProcessor;
import org.g2.dynamic.jdbc.interceptor.DynamicRoutWebMvcInterceptor;
import org.g2.dynamic.jdbc.loadbanlance.LoadBalanceBuildFactory;
import org.g2.dynamic.jdbc.loadbanlance.balancer.RoundRobinLoadBalancer;
import org.g2.dynamic.redis.hepler.RedisHelper;
import org.g2.dynamic.redis.hepler.dynamic.DynamicRedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author wuwenxi 2023-02-09
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DynamicJdbcProperties.class)
@ConditionalOnProperty(prefix = "g2.dynamic.jdbc", name = "enable", havingValue = "true")
public class DynamicJdbcAutoConfiguration {

    @Bean
    public DynamicRoutWebMvcInterceptor dynamicRoutWebMvcInterceptor() {
        return new DynamicRoutWebMvcInterceptor();
    }

    @Bean
    public WebMvcConfigurer addInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(dynamicRoutWebMvcInterceptor());
            }
        };
    }

    @Bean
    @ConditionalOnBean(DynamicRedisHelper.class)
    public RedisDatasourceFactory redisRoutDatasourceFactory(@Qualifier("dynamicRedisHelper") RedisHelper redisHelper) {
        return new RedisDatasourceFactory(redisHelper);
    }

    @Bean
    @ConditionalOnClass({HikariDataSource.class})
    @ConditionalOnMissingBean(
            name = {"defaultDataSourceFactory"}
    )
    public DataSourceFactory defaultDataSourceFactory() {
        return (dataSourceInfo -> {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(DynamicRoutResolver.resolve(dataSourceInfo, "driverClassName"));
            dataSource.setJdbcUrl(DynamicRoutResolver.resolve(dataSourceInfo, "jdbcUrl"));
            dataSource.setUsername(DynamicRoutResolver.resolve(dataSourceInfo, "username"));
            dataSource.setPassword(DynamicRoutResolver.resolve(dataSourceInfo, "password"));
            return dataSource;
        });
    }

    @Primary
    @Bean
    public DataSourceFactory dataSourceFactoryWrapper(DataSourceFactory dataSourceFactory,
                                                      @Autowired(required = false) List<DataSourceProcessor> dataSourceProcessors) {
        return new DataSourceFactoryWrapper(dataSourceFactory, dataSourceProcessors);
    }

    @Primary
    @Bean
    public DataSource dynamicRoutDataSource(@Qualifier("dataSource") DataSource dataSource, DataSourceFactory dataSourceFactory, DynamicJdbcProperties properties, MemoryDataSourceKeyFactory memoryDataSourceKeyFactory) {
        return new DynamicRoutDataSource(dataSource, dataSourceFactory, properties, memoryDataSourceKeyFactory);
    }

    @Bean
    public MemoryDataSourceKeyFactory memoryDataSourceKeyFactory(LoadBalanceBuildFactory loadBalanceBuildFactory) {
        return new MemoryDataSourceKeyFactory(loadBalanceBuildFactory);
    }

    @Bean
    @ConditionalOnMissingBean(value = LoadBalanceBuildFactory.class)
    public LoadBalanceBuildFactory loadBalanceBuildFactory() {
        return RoundRobinLoadBalancer::new;
    }

    @Bean
    public DataRoutInterceptor dataRoutInterceptor() {
        return new DataRoutInterceptor();
    }
}
