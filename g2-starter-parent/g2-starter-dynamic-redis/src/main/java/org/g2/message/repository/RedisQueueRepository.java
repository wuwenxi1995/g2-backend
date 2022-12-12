package org.g2.message.repository;

import java.time.Duration;

/**
 * @author wuwenxi 2022-12-07
 */
public interface RedisQueueRepository {

    /**
     * 添加数据
     *
     * @param key  key
     * @param data 数据
     */
    void push(String key, String data);

    /**
     * 添加数据
     *
     * @param db   redis库
     * @param key  key
     * @param data 数据
     */
    void push(int db, String key, String data);

    /**
     * 获取数据
     *
     * @param db          redis库
     * @param key         key
     * @param pollTimeout 拉取超时时间
     * @return 数据
     */
    String poll(int db, String key, Duration pollTimeout);

    /**
     * 数据处理成功, 提交数据
     *
     * @param db   redis库
     * @param key  key
     * @param data 提交数据
     */
    void commit(int db, String key, String data);

    /**
     * 数据处理失败, 数据回滚
     *
     * @param db    redis库
     * @param key   key
     * @param data  回滚数据
     * @param retry 重试次数
     */
    void rollback(int db, String key, String data, int retry);

    /**
     * 未自动提交
     *
     * @param db         redis库
     * @param key        key
     * @param ackTimeout ack超时时间
     * @return true/false
     */
    boolean ackTimeout(int db, String key, Duration ackTimeout);
}
