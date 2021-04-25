package org.g2.starter.redis.infra.customizer;

import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2021-04-22
 */
public interface CustomizerRedisListCommand<K, V> {

    /**
     * 返回并删除第一个元素
     * 阻塞操作，直到由元素可用或到达超时时间
     *
     * @param timeout 设置超时时间，设置为0的则一直等待直到有数据可取
     * @param key     不允许为空
     * @return 元素
     */
    List<V> bLeftPop(int timeout, K key);

    /**
     * 返回并删除最后一个元素
     * 阻塞操作，直到由元素可用或达到超时时间
     *
     * @param timeout 设置超时时间，设置为0的则一直等待直到有数据可取
     * @param key     不允许为空
     * @return 元素
     */
    List<V> bRightPop(int timeout, K key);

    /**
     * 删除srcKey队列最后一个元素，并将元素加入到dstKey队列，最终返回元素
     * 阻塞操作，直到由元素可用或达到超时时间
     *
     * @param timeout 设置超时时间，设置为0的则一直等待直到有数据可取
     * @param srcKey  需要删除的队列key，不允许为空
     * @param dstKey  添加元素的队列key，不允许为空
     * @return 操作的元素
     */
    V bRightPopLeftPush(int timeout, K srcKey, K dstKey);

}
