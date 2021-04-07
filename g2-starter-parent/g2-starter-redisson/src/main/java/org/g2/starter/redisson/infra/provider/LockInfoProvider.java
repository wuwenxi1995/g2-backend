package org.g2.starter.redisson.infra.provider;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.g2.starter.redisson.config.LockConfigureProperties;
import org.g2.starter.redisson.domain.LockInfo;
import org.g2.starter.redisson.infra.annotation.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;


/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Component
public class LockInfoProvider {
    private static final Logger log = LoggerFactory.getLogger(LockInfoProvider.class);

    private final ExpressionParser parser = new SpelExpressionParser();
    private final LockConfigureProperties lockConfigureProperties;

    public LockInfoProvider(LockConfigureProperties lockConfigureProperties) {
        this.lockConfigureProperties = lockConfigureProperties;
    }

    public LockInfo buildLockInfo(ProceedingJoinPoint joinPoint, Lock lock) {
        String lockName = lock.name();
        if (StringUtils.isNotBlank(lock.name())) {
            // 是否启用spel表达式
            if (lock.spel()) {
                // 获取调用方法
                Method method = getMethod(joinPoint);
                // 方法参数
                Parameter[] parameters = method.getParameters();
                Object[] parameterValues = joinPoint.getArgs();
                EvaluationContext context = new StandardEvaluationContext();

                Object value;
                for (int i = 0; i < parameterValues.length; i++) {
                    String name = parameters[i].getName();
                    value = parameterValues[i];
                    context.setVariable(name, value);
                }

                value = parser.parseExpression(lock.name()).getValue(context);
                if (Objects.nonNull(value)) {
                    lockName = String.valueOf(value);
                }
            }
        } else {
            // redis锁名称，默认：类名+方法名
            lockName = getLockName((MethodSignature) joinPoint.getSignature());
        }
        // 获取锁最长等待时间 可通过g2.lock.pattern.wait-time配置
        long waitTime = getLockTime(lock);
        // 获得锁后，自动释放锁的时间 可通过g2.lock.pattern.lease-time配置
        long leaseTime = getLeaseTime(lock);
        return new LockInfo(lockName, waitTime, leaseTime, lock.timeUnit());
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
            } catch (Exception e) {
                log.error("resolve method error : ", e);
            }
        }
        return method;
    }

    private String getLockName(MethodSignature signature) {
        return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
    }

    private long getLockTime(Lock lock) {
        return lock.waitTime() < 0 ? lockConfigureProperties.getProperty().getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(Lock lock) {
        return lock.leaseTime() < 0 ? lockConfigureProperties.getProperty().getLeaseTime() : lock.leaseTime();
    }
}
