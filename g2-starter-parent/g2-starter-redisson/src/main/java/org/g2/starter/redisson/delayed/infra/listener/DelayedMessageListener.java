package org.g2.starter.redisson.delayed.infra.listener;

/**
 * @author wuwenxi 2022-05-10
 */
public interface DelayedMessageListener<T> {

    /**
     * 延时队列监听事件
     *
     * @param message 监听事件
     */
    void onMessage(T message);

    /**
     * 延时队列名
     *
     * @return 队列名
     */
    default String queue() {
        return "";
    }
}
