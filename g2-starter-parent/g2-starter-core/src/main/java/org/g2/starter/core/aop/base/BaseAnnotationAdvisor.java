package org.g2.starter.core.aop.base;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.lang.NonNull;

/**
 * @author wuwenxi 2021-10-13
 * @see org.springframework.scheduling.annotation.AsyncAnnotationAdvisor
 */
public abstract class BaseAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    public BaseAnnotationAdvisor() {
        this.advice = buildAdvice();
        this.pointcut = buildPointcut();
    }

    @Override
    @NonNull
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    @NonNull
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    /**
     * 自定义拦截器
     *
     * @return advice
     */
    protected abstract Advice buildAdvice();

    /**
     * 自定义切入点
     *
     * @return pointcut
     */
    protected abstract Pointcut buildPointcut();
}
