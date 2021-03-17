package org.g2.boot.inf.config.spring;

import org.g2.boot.inf.infra.proxy.InfClientProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-16
 */
public class InfClientBeanProcessor implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private static final String INF_CLIENT_PROXY_CREATOR = "infClientProxyCreator";

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        // 注入客户端代理器
        RootBeanDefinition creator = new RootBeanDefinition();
        creator.setBeanClass(InfClientProxyCreator.class);
        creator.getPropertyValues().add("applicationContext", applicationContext);
        registry.registerBeanDefinition(INF_CLIENT_PROXY_CREATOR, creator);
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
