package org.g2.boot.inf.config.spring;

import org.g2.boot.inf.annotation.InfClient;
import org.g2.boot.inf.config.spring.scan.ClassPathInfClientScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-16
 */
public class InfClientBeanProcessor implements ApplicationContextAware, ResourceLoaderAware, BeanDefinitionRegistryPostProcessor {

    private String[] basePackages;
    private ResourceLoader resourceLoader;
    private ApplicationContext applicationContext;

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        ClassPathInfClientScanner scanner = new ClassPathInfClientScanner(registry, applicationContext);
        scanner.setResourceLoader(resourceLoader);
        scanner.resetFilters(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(InfClient.class));
        scanner.scan(basePackages);
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

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
