package org.g2.boot.log.infra.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.g2.boot.log.infra.annotation.LogRecord;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * 方法执行拦截器
 *
 * @author wuwenxi 2021-10-13
 * @see org.springframework.aop.interceptor.AsyncExecutionInterceptor
 * @see org.springframework.scheduling.annotation.AnnotationAsyncExecutionInterceptor
 */
public class LogRecordExecutionInterceptor implements MethodInterceptor, BeanFactoryAware {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        final Method method = BridgeMethodResolver.findBridgedMethod(specificMethod);
        LogRecord logRecord = AnnotatedElementUtils.findMergedAnnotation(method, LogRecord.class);
        if (logRecord == null) {
            AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), LogRecord.class);
        }
        if (logRecord == null) {
            return invocation.proceed();
        }
        return null;
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {

    }
}
