package org.g2.starter.redisson.infra.provider;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.g2.starter.redisson.config.LockConfigureProperties;
import org.g2.starter.redisson.domain.LockInfo;
import org.g2.starter.redisson.infra.annotation.Lock;
import org.springframework.stereotype.Component;


/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Component
public class LockInfoProvider {

    private final LockConfigureProperties lockConfigureProperties;

    public LockInfoProvider(LockConfigureProperties lockConfigureProperties) {
        this.lockConfigureProperties = lockConfigureProperties;
    }

    public LockInfo buildLockInfo(ProceedingJoinPoint joinPoint, Lock lock) {
        // redis的key值，默认：类名+方法名
        String lockName = StringUtils.isBlank(lock.name()) ? String.format("%s.%s", joinPoint.getSignature().getDeclaringTypeName(), ((MethodSignature) joinPoint.getSignature()).getMethod().getName()) : lock.name();
        // 获取锁最长等待时间 可通过g2.lock.pattern.wait-time配置
        long waitTime = (lock.waitTime() == Integer.MIN_VALUE || lock.waitTime() < 0) ? lockConfigureProperties.getProperty().getWaitTime() : lock.waitTime();
        // 获得锁后，自动释放锁的时间 可通过g2.lock.pattern.lease-time配置
        long leaseTime = (lock.leaseTime() == Integer.MIN_VALUE || lock.leaseTime() < 0) ? lockConfigureProperties.getProperty().getLeaseTime() : lock.leaseTime();
        return new LockInfo(lockName, waitTime, leaseTime, lock.timeUnit());
    }
}
