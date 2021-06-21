package org.g2.starter.lock.infra.service;

import org.g2.starter.lock.domain.LockInfo;

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
