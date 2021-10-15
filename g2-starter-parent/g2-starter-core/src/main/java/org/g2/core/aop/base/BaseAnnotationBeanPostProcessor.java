package org.g2.core.aop.base;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author wuwenxi 2021-10-13
 * @see org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor
 */
public abstract class BaseAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.advisor = buildAdvisor(beanFactory);
    }

    /**
     * 自定义增强器
     *
     * @param beanFactory beanFactory
     * @return 增强器
     */
    protected abstract Advisor buildAdvisor(BeanFactory beanFactory);
}
