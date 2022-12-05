package org.g2.starter.redisson.lock.infra.provider;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.g2.starter.redisson.lock.config.RedissonConfigureProperties;
import org.g2.starter.redisson.lock.domain.LockInfo;
import org.g2.starter.redisson.lock.infra.annotation.Lock;
import org.g2.starter.redisson.lock.infra.annotation.LockKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Component
public class LockInfoProvider {
    private static final Logger log = LoggerFactory.getLogger(LockInfoProvider.class);

    private final ExpressionParser parser = new SpelExpressionParser();
    private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final RedissonConfigureProperties redissonConfigureProperties;

    public LockInfoProvider(RedissonConfigureProperties redissonConfigureProperties) {
        this.redissonConfigureProperties = redissonConfigureProperties;
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
            // redis锁名称，默认：类名+方法名+keys
            List<String> keyList = this.getKeyList(joinPoint, lock.keys());
            lockName = getLockName((MethodSignature) joinPoint.getSignature(), keyList);
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

    private String getLockName(MethodSignature signature, List<String> keyList) {
        return String.format("%s.%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName(),
                org.springframework.util.StringUtils.collectionToDelimitedString(keyList, "", "-", ""));
    }

    private long getLockTime(Lock lock) {
        return lock.waitTime() < 0 ? redissonConfigureProperties.getProperty().getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(Lock lock) {
        return lock.leaseTime() < 0 ? redissonConfigureProperties.getProperty().getLeaseTime() : lock.leaseTime();
    }

    private List<String> getKeyList(ProceedingJoinPoint joinPoint, String[] keys) {
        Method method = getMethod(joinPoint);
        List<String> keyList = new ArrayList<>();
        if (keys.length != 0) {
            List<String> spelDefinitionKey = getSpelDefinitionKey(keys, method, joinPoint.getArgs());
            keyList.addAll(spelDefinitionKey);
        }
        List<String> parameterKeys = getParameterKey(method.getParameters(), joinPoint.getArgs());
        keyList.addAll(parameterKeys);
        return keyList;
    }

    private List<String> getSpelDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList<>();
        for (String definitionKey : definitionKeys) {
            EvaluationContext context =
                    new MethodBasedEvaluationContext(null, method, parameterValues, nameDiscoverer);
            Object value = parser.parseExpression(definitionKey).getValue(context);
            if (value != null) {
                definitionKeyList.add(value.toString());
            }
        }
        return definitionKeyList;
    }

    /**
     * 获取参数KEY
     */
    private List<String> getParameterKey(Parameter[] parameters, Object[] parameterValues) {
        List<String> parameterKey = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            // 单个LockKey?
            if (parameters[i].getAnnotation(LockKey.class) != null) {
                LockKey keyAnnotation = parameters[i].getAnnotation(LockKey.class);
                if (keyAnnotation.value().isEmpty()) {
                    parameterKey.add(parameterValues[i].toString());
                } else {
                    StandardEvaluationContext context = new StandardEvaluationContext(parameterValues[i]);
                    Object value = parser.parseExpression(keyAnnotation.value()).getValue(context);
                    if (value != null) {
                        parameterKey.add(value.toString());
                    }
                }
            }
        }
        return parameterKey;
    }
}
