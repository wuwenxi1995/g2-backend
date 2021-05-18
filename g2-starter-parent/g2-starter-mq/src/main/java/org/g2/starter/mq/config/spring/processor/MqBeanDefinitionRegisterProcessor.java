package org.g2.starter.mq.config.spring.processor;

import org.g2.starter.mq.listener.annotation.Listener;
import org.g2.starter.mq.config.spring.scan.ClassPathMqScanner;
import org.g2.starter.mq.subject.annotation.Subject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;

/**
 * @author wenxi.wu@hand-chian.com 2021-05-17
 */
public class MqBeanDefinitionRegisterProcessor implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware {

    private String[] basePackages;

    private ResourceLoader resourceLoader;

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        ClassPathMqScanner scanner = new ClassPathMqScanner(registry);
        scanner.setResourceLoader(resourceLoader);
        scanner.resetFilters(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Listener.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Subject.class));
        scanner.scan(this.basePackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
