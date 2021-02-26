package org.g2.starter.redisson.infra.service.impl;

import org.g2.starter.redisson.infra.enums.LockType;
import org.g2.starter.redisson.infra.service.LockStrategy;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 分布式锁--联锁
 *
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class MultiLockStrategy extends LockStrategy {

    @Qualifier("lockRedissonClient")
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean lock() {
        RLock[] rLocks = new RLock[lockInfo.getKeyList().size()];
        for (int i = 0; i < rLocks.length; i++) {
            rLocks[i] = redissonClient.getLock(lockInfo.getKeyList().get(i));
        }
        try {
            rLock = new RedissonMultiLock(rLocks);
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), lockInfo.getTimeUnit());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void unLock() {
        rLock.unlock();
    }

    @Override
    public LockType lockType() {
        return LockType.MULTI;
    }
}
