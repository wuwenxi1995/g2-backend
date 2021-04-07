package org.g2.starter.redisson.infra.service;

import org.g2.starter.redisson.domain.LockInfo;
import org.g2.starter.redisson.infra.enums.LockType;
import org.redisson.api.RLock;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public abstract class LockStrategy implements LockService {

    protected ThreadLocal<LockInfo> lockInfoThreadLocal = new ThreadLocal<>();

    /**
     * 设置分布式锁信息
     *
     * @param lockInfo 锁信息
     */
    public final void setLockInfo(LockInfo lockInfo) {
        lockInfoThreadLocal.set(lockInfo);
    }

    public final LockInfo getLockInfo() {
        return lockInfoThreadLocal.get();
    }

    public final void clear() {
        lockInfoThreadLocal.remove();
    }
}
