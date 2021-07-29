package org.g2.starter.aop.config.spring.scanner;

import org.apache.commons.collections4.CollectionUtils;
import org.g2.core.util.StringUtil;
import org.g2.starter.aop.infra.advisor.CustomAdvisor;
import org.g2.starter.aop.infra.constant.InterceptorConstant;
import org.g2.starter.aop.infra.pointcut.CustomPointCut;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * @author wuwenxi 2021-07-29
 */
public class InterceptorScanner extends ClassPathBeanDefinitionScanner {

    private static final String BEAN_NAME = "beanName";
    private static final String APPLICATION_CONTEXT = "applicationContext";

    private BeanDefinitionRegistry registry;

    private ApplicationContext applicationContext;

    public InterceptorScanner(BeanDefinitionRegistry registry) {
        super(registry);
        this.registry = registry;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    @NonNull
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> definitionHolders = super.doScan(basePackages);
        if (CollectionUtils.isNotEmpty(definitionHolders)) {
            for (BeanDefinitionHolder holder : definitionHolders) {
                // 注入自定义方法拦截CustomPointCut
                RootBeanDefinition pointCut = new RootBeanDefinition();
                pointCut.setBeanClass(CustomPointCut.class);
                pointCut.getPropertyValues().add(APPLICATION_CONTEXT, applicationContext);
                pointCut.getPropertyValues().add(BEAN_NAME, holder.getBeanName());
                registry.registerBeanDefinition(StringUtil.getBeanName(InterceptorConstant.POINT_CUT_PREFIX, holder.getBeanName()), pointCut);
                // 注入自定义增强器CustomAdvisor
                RootBeanDefinition advisor = new RootBeanDefinition();
                advisor.setBeanClass(CustomAdvisor.class);
                advisor.getPropertyValues().add(APPLICATION_CONTEXT, applicationContext);
                advisor.getPropertyValues().add(BEAN_NAME, holder.getBeanName());
                registry.registerBeanDefinition(StringUtil.getBeanName(InterceptorConstant.ADVISOR_PREFIX, holder.getBeanName()), advisor);
            }
        }
        return definitionHolders;
    }
}
