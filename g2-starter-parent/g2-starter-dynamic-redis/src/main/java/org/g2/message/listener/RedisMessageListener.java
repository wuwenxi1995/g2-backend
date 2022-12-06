package org.g2.message.listener;

/**
 * @author wuwenxi 2022-12-06
 */
public interface RedisMessageListener<T> {

    /**
     * 消息监听
     *
     * @param message message
     */
    void onMessage(T message);
}
