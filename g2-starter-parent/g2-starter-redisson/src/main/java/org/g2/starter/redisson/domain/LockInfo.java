package org.g2.starter.redisson.domain;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-22
 */
public class LockInfo {

    private String name;
    private long waitTime;
    private long leaseTime;
    private TimeUnit timeUnit;
    /**
     * 多个锁
     */
    private List<String> keyList;

    public LockInfo() {
        this.timeUnit = TimeUnit.SECONDS;
    }

    public LockInfo(String name, long waitTime, long leaseTime, TimeUnit timeUnit) {
        this.name = name;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
        this.timeUnit = timeUnit;
    }

    public LockInfo(String name, long waitTime, long leaseTime, TimeUnit timeUnit, List<String> keyList) {
        this(name, waitTime, leaseTime, timeUnit);
        this.keyList = keyList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }
}
