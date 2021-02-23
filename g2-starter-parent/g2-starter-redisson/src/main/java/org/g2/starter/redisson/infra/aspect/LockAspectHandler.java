package org.g2.starter.redisson.infra.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.g2.core.exception.CommonException;
import org.g2.starter.redisson.domain.LockInfo;
import org.g2.starter.redisson.infra.annotation.Lock;
import org.g2.starter.redisson.infra.factory.LockStrategyFactory;
import org.g2.starter.redisson.infra.provider.LockInfoProvider;
import org.g2.starter.redisson.infra.service.LockStrategy;
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

    /**
     * 存储对应线程的锁信息
     */
    private ThreadLocal<LockStrategy> currentThreadLock = new ThreadLocal<>();
    /**
     * 存储当前线程是否加锁成功
     */
    private ThreadLocal<Boolean> currentThreadLockReleaseLock = new ThreadLocal<>();

    private final LockStrategyFactory lockStrategyFactory;
    private final LockInfoProvider lockInfoProvider;

    public LockAspectHandler(LockStrategyFactory lockStrategyFactory, LockInfoProvider lockInfoProvider) {
        this.lockStrategyFactory = lockStrategyFactory;
        this.lockInfoProvider = lockInfoProvider;
    }

    @Around(value = "@annotation(lock)")
    public Object around(ProceedingJoinPoint joinPoint, Lock lock) throws Throwable {
        currentThreadLockReleaseLock.set(false);
        // 获取锁类型
        LockStrategy lockStrategy = lockStrategyFactory.getLockStrategy(lock.lockType());
        // 构建锁信息
        LockInfo lockInfo = lockInfoProvider.buildLockInfo(joinPoint, lock);
        lockStrategy.setLockInfo(lockInfo);
        // 加锁
        if (lockStrategy.lock()) {
            currentThreadLockReleaseLock.set(true);
            currentThreadLock.set(lockStrategy);
            return joinPoint.proceed();
        }
        throw new CommonException("Get Lock failed");
    }

    @AfterReturning(value = "@annotation(lock)")
    public void afterReturning(Lock lock) {
        if (currentThreadLockReleaseLock.get()) {
            currentThreadLock.get().unLock();
        }
        currentThreadLock.remove();
        currentThreadLockReleaseLock.remove();
    }

    @AfterThrowing(value = "@annotation(lock)")
    public void afterThrowing(Lock lock) {
        if (currentThreadLockReleaseLock.get()) {
            currentThreadLock.get().unLock();
            currentThreadLock.remove();
            currentThreadLockReleaseLock.remove();
        }
    }
}
