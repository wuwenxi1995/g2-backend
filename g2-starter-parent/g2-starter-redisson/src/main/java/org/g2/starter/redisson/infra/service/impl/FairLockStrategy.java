package org.g2.starter.redisson.infra.service.impl;

import org.g2.starter.redisson.infra.enums.LockType;
import org.g2.starter.redisson.infra.service.LockStrategy;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 分布式锁--公平锁
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class FairLockStrategy extends LockStrategy {

    @Qualifier("lockRedissonClient")
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean lock() {
        try {
            this.rLock = redissonClient.getFairLock(lockInfo.getName());
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
        return LockType.FAIR;
    }
}
