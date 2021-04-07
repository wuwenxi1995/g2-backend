package org.g2.starter.redisson.infra.service.impl;

import org.g2.starter.redisson.domain.LockInfo;
import org.g2.starter.redisson.infra.enums.LockType;
import org.g2.starter.redisson.infra.service.LockStrategy;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 分布式锁--重入锁
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class ReentrantLockStrategy extends LockStrategy {

    @Qualifier("lockRedissonClient")
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean lock() {
        LockInfo lockInfo = lockInfoThreadLocal.get();
        try {
            RLock rLock = redissonClient.getLock(lockInfo.getName());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), lockInfo.getTimeUnit());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void unLock() {
        LockInfo lockInfo = lockInfoThreadLocal.get();
        RLock rLock = redissonClient.getLock(lockInfo.getName());
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlockAsync();
        }
        lockInfoThreadLocal.remove();
    }

    @Override
    public LockType lockType() {
        return LockType.REENTRANT;
    }
}
