package org.g2.boot.message.service;

/**
 * @author wenxi.wu@hand-chian.com 2021-02-08
 */
public interface Publisher {

    /**
     * 通过userId发布消息
     *
     * @param userId  用户id
     * @param key     订阅key
     * @param message 消息内容
     */
    void sendByUser(Long userId, String key, String message);

    /**
     * 通过sessionId发布消息
     *
     * @param sessionId session信息
     * @param key       订阅key
     * @param message   消息内容
     */
    void sendBySession(String sessionId, String key, String message);

    /**
     * 给所有人发布消息
     *
     * @param key     订阅消息
     * @param message 消息内容
     */
    void sendToAll(String key, String message);
}
