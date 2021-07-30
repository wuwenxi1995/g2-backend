package org.g2.core.aop.infra.advisor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * @author wuwenxi 2021-07-29
 */
public class CustomAdvisor extends AbstractPointcutAdvisor {

    private BeanFactory beanFactory;
    private String beanName;
    private Advice advice;

    public void setAdvice(Object advice) {
        Assert.isTrue(advice instanceof Advice, "CustomAdvisor require an advice");
        this.advice = (Advice) advice;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    @NonNull
    public Pointcut getPointcut() {
        return beanFactory.getBean(beanName, Pointcut.class);
    }

    @Override
    @NonNull
    public Advice getAdvice() {
        return advice;
    }
}
