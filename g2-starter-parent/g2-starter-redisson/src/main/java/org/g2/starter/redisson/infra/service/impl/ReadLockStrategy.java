package org.g2.starter.redisson.infra.service.impl;

import org.g2.starter.redisson.infra.enums.LockType;
import org.g2.starter.redisson.infra.service.LockStrategy;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 分布式锁--读锁
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class ReadLockStrategy extends LockStrategy {

    @Qualifier("lockRedissonClient")
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean lock() {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockInfo.getName());
        try {
            rLock = readWriteLock.readLock();
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), lockInfo.getTimeUnit());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void unLock() {
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlockAsync();
        }
    }

    @Override
    public LockType lockType() {
        return LockType.READ;
    }
}
