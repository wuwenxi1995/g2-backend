package org.g2.starter.redis.infra.queue;

import java.util.List;

/**
 * redis 发布/订阅 队列类
 *
 * @author wenxi.wu@hand-china.com 2020-11-06
 */
public interface IQueue {

    /**
     * 数据推入队列
     *
     * @param key   键
     * @param value 值
     */
    void push(String key, Object value);

    /**
     * 数据推入队列
     *
     * @param key   键
     * @param value 值
     */
    void push(String key, String value);

    /**
     * 取出数据
     *
     * @param key 键值
     * @return 值
     */
    String pull(String key);

    /**
     * 取出全部数据
     *
     * @param key 键
     * @return 值
     */
    List<String> pullAll(String key);

    /**
     * 取出指定数量数据，从第一个数据到结束下标
     *
     * @param key 键
     * @param end 结束下标
     * @return 值
     */
    List<String> pullAll(String key, int end);

    /**
     * 取出指定位置到数据
     *
     * @param key   键
     * @param start 开始下标
     * @param end   结束下标
     * @return 值
     */
    List<String> pullAll(String key, int start, int end);
}
