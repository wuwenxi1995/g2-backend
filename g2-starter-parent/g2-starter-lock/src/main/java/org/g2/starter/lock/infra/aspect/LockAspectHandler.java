package org.g2.starter.lock.infra.aspect;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.g2.starter.lock.config.factory.LockStrategyFactory;
import org.g2.starter.lock.domain.LockInfo;
import org.g2.starter.lock.infra.annotation.Lock;
import org.g2.starter.lock.infra.annotation.Mutex;
import org.g2.starter.lock.infra.exception.LockException;
import org.g2.starter.lock.infra.provider.LockInfoProvider;
import org.g2.starter.lock.infra.service.LockStrategy;
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
    private final CuratorFramework curator;

    public LockAspectHandler(LockStrategyFactory lockStrategyFactory, LockInfoProvider lockInfoProvider, CuratorFramework curator) {
        this.lockStrategyFactory = lockStrategyFactory;
        this.lockInfoProvider = lockInfoProvider;
        this.curator = curator;
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
        boolean lockFlag = false;
        // 加锁
        try {
            boolean success = lockStrategy.lock();
            if (!success) {
                throw new LockException("Get Lock failed");
            }
            lockFlag = true;
            return joinPoint.proceed();
        } finally {
            if (lockFlag) {
                lockStrategy.unLock();
            }
        }
    }

    @Around(value = "@annotation(mutex)")
    public Object around(ProceedingJoinPoint joinPoint, Mutex mutex) throws Throwable {
        InterProcessMutex lock = new InterProcessMutex(curator, lockInfoProvider.buildPath(mutex));
        boolean lockFlag = false;
        try {
            boolean success = lock.acquire(mutex.waitTime(), mutex.timeUnit());
            if (!success) {
                throw new LockException("Get Lock failed");
            }
            lockFlag = true;
            return joinPoint.proceed();
        } finally {
            if (lockFlag) {
                lock.release();
            }
        }
    }
}
