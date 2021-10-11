package org.g2.starter.redisson.lock.infra.enums;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public enum LockType {
    /**
     * 公平锁
     */
    FAIR,
    /**
     * 非公平锁
     */
    REENTRANT,
    /**
     * 多重锁
     */
    MULTI,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE,
    /**
     * 红锁
     */
    RED;

    LockType() {
    }
}
