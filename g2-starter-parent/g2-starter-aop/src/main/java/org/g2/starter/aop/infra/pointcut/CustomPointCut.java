package org.g2.starter.aop.infra.pointcut;

import org.g2.starter.aop.annotation.Interceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author wuwenxi 2021-07-29
 */
public class CustomPointCut extends StaticMethodMatcherPointcut implements InitializingBean {

    private ApplicationContext applicationContext;
    private String beanName;
    private String[] methodName;
    private Class<?> target;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
        return Arrays.stream(methodName).anyMatch(name -> Objects.equals(method.getName(), name));
    }

    @Override
    @NonNull
    public ClassFilter getClassFilter() {
        return clazz -> target.isAssignableFrom(clazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Object bean = this.applicationContext.getBean(beanName);
        Interceptor interceptor = AnnotationUtils.findAnnotation(bean.getClass(), Interceptor.class);
        if (interceptor != null) {
            this.target = interceptor.clazz();
            this.methodName = interceptor.method();
        }
    }
}
