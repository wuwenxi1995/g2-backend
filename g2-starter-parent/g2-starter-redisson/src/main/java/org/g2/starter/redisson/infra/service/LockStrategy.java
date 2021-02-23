package org.g2.starter.redisson.infra.service;

import org.g2.starter.redisson.domain.LockInfo;
import org.g2.starter.redisson.infra.enums.LockType;
import org.redisson.api.RLock;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public abstract class LockStrategy {

    protected LockInfo lockInfo;
    protected RLock rLock;

    /**
     * 设置分布式锁信息
     *
     * @param lockInfo 锁信息
     */
    public final void setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
    }

    /**
     * 加锁
     *
     * @return 成功/失败
     */
    public abstract boolean lock();

    /**
     * 释放锁
     */
    public abstract void unLock();

    /**
     * 分布式锁类型
     *
     * @return 锁类型
     */
    public abstract LockType lockType();
}
