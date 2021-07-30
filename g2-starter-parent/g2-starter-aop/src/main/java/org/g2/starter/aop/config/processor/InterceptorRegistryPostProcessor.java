package org.g2.starter.aop.config.processor;

import org.g2.core.util.StringUtil;
import org.g2.starter.aop.annotation.Interceptor;
import org.g2.starter.aop.infra.advisor.CustomAdvisor;
import org.g2.starter.aop.infra.constant.InterceptorConstant;
import org.g2.starter.aop.infra.pointcut.CustomPointCut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * @author wuwenxi 2021-07-30
 */
public class InterceptorRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private static final String BEAN_NAME = "beanName";
    private static final String BEAN_FACTORY = "beanFactory";

    private BeanDefinitionRegistry registry;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Object> beans = beanFactory.getBeansWithAnnotation(Interceptor.class);
        for (String beanName : beans.keySet()) {
            // 注入自定义方法拦截CustomPointCut
            RootBeanDefinition pointCut = new RootBeanDefinition();
            pointCut.setBeanClass(CustomPointCut.class);
            pointCut.getPropertyValues().add(BEAN_FACTORY, beanFactory);
            pointCut.getPropertyValues().add(BEAN_NAME, beanName);
            registry.registerBeanDefinition(StringUtil.getBeanName(InterceptorConstant.POINT_CUT_PREFIX, beanName), pointCut);
            // 注入自定义增强器CustomAdvisor
            RootBeanDefinition advisor = new RootBeanDefinition();
            advisor.setBeanClass(CustomAdvisor.class);
            advisor.getPropertyValues().add(BEAN_FACTORY, beanFactory);
            advisor.getPropertyValues().add(BEAN_NAME, beanName);
            registry.registerBeanDefinition(StringUtil.getBeanName(InterceptorConstant.ADVISOR_PREFIX, beanName), advisor);
        }
    }
}
