package org.g2.core.aop.config.processor;

import org.aopalliance.intercept.MethodInterceptor;
import org.g2.core.aop.annotation.Interceptor;
import org.g2.core.aop.infra.advisor.CustomAdvisor;
import org.g2.core.aop.infra.constant.InterceptorConstant;
import org.g2.core.aop.infra.pointcut.CustomPointCut;
import org.g2.core.util.StringUtil;
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
    private static final String ADVICE = "advice";

    private BeanDefinitionRegistry registry;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Object> beans = beanFactory.getBeansWithAnnotation(Interceptor.class);
        beans.forEach((beanName, bean) -> {
            if (!(bean instanceof MethodInterceptor)) {
                return;
            }
            // 注入自定义方法拦截点CustomPointCut
            RootBeanDefinition pointcut = new RootBeanDefinition();
            pointcut.setBeanClass(CustomPointCut.class);
            pointcut.getPropertyValues().add(ADVICE, bean);
            String pointcutBeanName = StringUtil.getBeanName(InterceptorConstant.POINT_CUT_PREFIX, beanName);
            registry.registerBeanDefinition(pointcutBeanName, pointcut);
            // 注入自定义增强器CustomAdvisor
            RootBeanDefinition advisor = new RootBeanDefinition();
            advisor.setBeanClass(CustomAdvisor.class);
            advisor.getPropertyValues().add(ADVICE, bean);
            advisor.getPropertyValues().add(BEAN_FACTORY, beanFactory);
            advisor.getPropertyValues().add(BEAN_NAME, pointcutBeanName);
            registry.registerBeanDefinition(StringUtil.getBeanName(InterceptorConstant.ADVISOR_PREFIX, beanName), advisor);
        });
    }
}
