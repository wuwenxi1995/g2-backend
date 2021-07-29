package org.g2.starter.aop.infra.advisor;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.g2.core.util.StringUtil;
import org.g2.starter.aop.infra.constant.InterceptorConstant;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

/**
 * @author wuwenxi 2021-07-29
 */
public class CustomAdvisor extends AbstractPointcutAdvisor implements InitializingBean {

    private ApplicationContext applicationContext;
    private String beanName;
    private Pointcut pointcut;
    private Advice advice;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    @NonNull
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    @NonNull
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.advice = applicationContext.getBean(beanName, Advice.class);
        this.pointcut = applicationContext.getBean(StringUtil.getBeanName(InterceptorConstant.POINT_CUT_PREFIX, beanName), Pointcut.class);
    }
}
