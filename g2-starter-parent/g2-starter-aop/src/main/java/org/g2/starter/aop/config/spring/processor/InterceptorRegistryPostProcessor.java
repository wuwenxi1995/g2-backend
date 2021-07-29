package org.g2.starter.aop.config.spring.processor;

import org.aopalliance.intercept.MethodInterceptor;
import org.g2.starter.aop.annotation.Interceptor;
import org.g2.starter.aop.config.spring.scanner.InterceptorScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;

/**
 * @author wuwenxi 2021-07-29
 */
public class InterceptorRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, ApplicationContextAware {

    private String[] basePackages;
    private ResourceLoader resourceLoader;
    private ApplicationContext applicationContext;

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        InterceptorScanner scanner = new InterceptorScanner(registry);
        scanner.setResourceLoader(resourceLoader);
        scanner.resetFilters(Boolean.FALSE);
        // 过滤未使用@Interceptor注解的类
        scanner.addIncludeFilter(new AnnotationTypeFilter(Interceptor.class));
        // 过滤类型不是MethodInterceptor的类
        scanner.addIncludeFilter(new AssignableTypeFilter(MethodInterceptor.class));
        scanner.setApplicationContext(applicationContext);
        scanner.scan(this.basePackages);
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
