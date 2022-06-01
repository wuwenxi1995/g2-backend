package org.g2.starter.redisson.lock.infra.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.g2.starter.redisson.lock.config.factory.LockStrategyFactory;
import org.g2.starter.redisson.lock.domain.LockInfo;
import org.g2.starter.redisson.lock.infra.annotation.Lock;
import org.g2.starter.redisson.lock.infra.exception.LockException;
import org.g2.starter.redisson.lock.infra.provider.LockInfoProvider;
import org.g2.starter.redisson.lock.infra.service.LockStrategy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 拦截注有@Lock注解的方法
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
@Aspect
@Component
@Order(0)
public class LockAspectHandler {

    private final LockStrategyFactory lockStrategyFactory;
    private final LockInfoProvider lockInfoProvider;

    public LockAspectHandler(LockStrategyFactory lockStrategyFactory, LockInfoProvider lockInfoProvider) {
        this.lockStrategyFactory = lockStrategyFactory;
        this.lockInfoProvider = lockInfoProvider;
    }

    @Around(value = "@annotation(lock)")
    public Object around(ProceedingJoinPoint joinPoint, Lock lock) throws Throwable {
        // 构建锁信息
        LockInfo lockInfo = lockInfoProvider.buildLockInfo(joinPoint, lock);
        // 获取锁类型
        LockStrategy lockStrategy = lockStrategyFactory.getLockStrategy(lock.lockType());
        if (lockStrategy.getLockInfo() != null && !lockInfo.equals(lockStrategy.getLockInfo())) {
            return joinPoint.proceed();
        }
        lockStrategy.setLockInfo(lockInfo);
        boolean success = lockStrategy.lock();
        if (!success) {
            throw new LockException("Get Lock failed");
        }
        // 加锁
        try {
            return joinPoint.proceed();
        } finally {
            lockStrategy.unLock();
        }
    }
}
