package org.g2.starter.lock.infra.service;

import org.g2.starter.lock.infra.enums.LockType;

/**
 * 查考redisson文档 https://github.com/redisson/redisson/wiki/8.-distributed-locks-and-synchronizers
 *
 * @author wenxi.wu@hand-chian.com 2021-04-07
 */
public interface LockService {

    /**
     * 加锁
     *
     * @return 成功/失败
     */
    boolean lock();

    /**
     * 释放锁
     */
    void unLock();

    /**
     * 分布式锁类型
     *
     * @return 锁类型
     */
    LockType lockType();
}
