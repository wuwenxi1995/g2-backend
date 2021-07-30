package org.g2.core.aop.infra.pointcut;

import org.g2.core.aop.annotation.Interceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author wuwenxi 2021-07-29
 */
public class CustomPointCut extends StaticMethodMatcherPointcut {

    private String[] methodName;
    private Class<?> target;

    public void setAdvice(Object advice) {
        Interceptor interceptor = AnnotationUtils.findAnnotation(advice.getClass(), Interceptor.class);
        Assert.notNull(interceptor, "Interceptor must not be null");
        this.methodName = interceptor.method();
        this.target = interceptor.clazz();
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
}
