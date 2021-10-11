package org.g2.starter.redisson.lock.infra.service.impl;

import org.g2.starter.redisson.lock.domain.LockInfo;
import org.g2.starter.redisson.lock.infra.enums.LockType;
import org.g2.starter.redisson.lock.infra.service.LockStrategy;
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
        LockInfo lockInfo = getLockInfo();
        RLock[] rLocks = new RLock[lockInfo.getKeyList().size()];
        for (int i = 0; i < rLocks.length; i++) {
            rLocks[i] = redissonClient.getLock(lockInfo.getKeyList().get(i));
        }
        try {
            RLock rLock = new RedissonMultiLock(rLocks);
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), lockInfo.getTimeUnit());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void unLock() {
        LockInfo lockInfo = getLockInfo();
        RLock[] lockList = new RLock[lockInfo.getKeyList().size()];

        for(int i = 0; i < lockInfo.getKeyList().size(); ++i) {
            lockList[i] = this.redissonClient.getLock(lockInfo.getKeyList().get(i));
        }

        RedissonMultiLock lock = new RedissonMultiLock(lockList);
        lock.unlock();
        clear();
    }

    @Override
    public LockType lockType() {
        return LockType.MULTI;
    }
}
