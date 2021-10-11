package org.g2.starter.redisson.lock.infra.provider;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.g2.core.base.BaseConstants;
import org.g2.starter.redisson.lock.config.RedissonConfigureProperties;
import org.g2.starter.redisson.lock.domain.LockInfo;
import org.g2.starter.redisson.lock.infra.annotation.Lock;
import org.g2.starter.redisson.lock.infra.annotation.Mutex;
import org.g2.starter.redisson.lock.infra.constants.LockConstants;
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
            // redis锁名称，默认：类名+方法名
            lockName = getLockName((MethodSignature) joinPoint.getSignature());
        }
        // 获取锁最长等待时间 可通过g2.lock.pattern.wait-time配置
        long waitTime = getLockTime(lock);
        // 获得锁后，自动释放锁的时间 可通过g2.lock.pattern.lease-time配置
        long leaseTime = getLeaseTime(lock);
        return new LockInfo(lockName, waitTime, leaseTime, lock.timeUnit());
    }

    public String buildPath(Mutex mutex) {
        String path;
        if (!(path = mutex.path()).startsWith(BaseConstants.Symbol.SLASH)) {
            path = BaseConstants.Symbol.SLASH + path;
        }
        return LockConstants.ZK_LOCK_NAME_SPACE + path;
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
        return lock.waitTime() < 0 ? redissonConfigureProperties.getProperty().getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(Lock lock) {
        return lock.leaseTime() < 0 ? redissonConfigureProperties.getProperty().getLeaseTime() : lock.leaseTime();
    }
}
