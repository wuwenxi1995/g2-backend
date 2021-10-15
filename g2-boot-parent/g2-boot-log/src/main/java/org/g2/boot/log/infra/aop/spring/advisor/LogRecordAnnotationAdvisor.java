package org.g2.boot.log.infra.aop.spring.advisor;

import org.aopalliance.aop.Advice;
import org.g2.boot.log.infra.annotation.LogRecord;
import org.g2.boot.log.infra.aop.interceptor.LogRecordExecutionInterceptor;
import org.g2.core.aop.base.BaseAnnotationAdvisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 自定义增强器
 *
 * @author wuwenxi 2021-10-13
 */
public class LogRecordAnnotationAdvisor extends BaseAnnotationAdvisor {

    @Override
    protected Advice buildAdvice() {
        return new LogRecordExecutionInterceptor();
    }

    @Override
    protected Pointcut buildPointcut() {
        // 检查类上面的注解
        Pointcut cpc = new AnnotationMatchingPointcut(LogRecord.class, true);
        // 检查方法上的注解
        Pointcut mpc = new AnnotationMatchingPointcut(null, LogRecord.class, true);
        ComposablePointcut pointcut = new ComposablePointcut(cpc);
        pointcut.union(mpc);
        return pointcut;
    }
}
