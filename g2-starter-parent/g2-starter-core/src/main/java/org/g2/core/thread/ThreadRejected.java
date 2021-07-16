package org.g2.core.thread;

/**
 * 线程拒绝策略
 *
 * @author wuwenxi 2021-07-16
 */
public enum ThreadRejected {

    /**
     * 拒绝任务，抛出异常
     */
    AbortPolicy("abortPolicy"),
    /**
     * 丢弃新提交的任务
     */
    DiscardPolicy("DiscardPolicy"),
    /**
     * 丢弃等待队列中第一个任务
     */
    DiscardOldestPolicy("DiscardOldestPolicy"),
    /**
     * 在调用线程中执行当前任务
     */
    CallerRunsPolicy("CallerRunsPolicy");

    String value;

    ThreadRejected(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
