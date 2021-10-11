package org.g2.starter.lock.infra.service.impl;

import org.g2.starter.lock.domain.LockInfo;
import org.g2.starter.lock.infra.enums.LockType;
import org.g2.starter.lock.infra.service.LockStrategy;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 分布式锁--写锁
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class WriteLockStrategy extends LockStrategy {

    @Qualifier("lockRedissonClient")
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean lock() {
        LockInfo lockInfo = getLockInfo();
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockInfo.getName());
        try {
            RLock rLock = readWriteLock.writeLock();
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), lockInfo.getTimeUnit());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void unLock() {
        LockInfo lockInfo = getLockInfo();
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockInfo.getName());
        RLock rLock = readWriteLock.writeLock();
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlockAsync();
        }
        clear();
    }

    @Override
    public LockType lockType() {
        return LockType.WRITE;
    }
}
