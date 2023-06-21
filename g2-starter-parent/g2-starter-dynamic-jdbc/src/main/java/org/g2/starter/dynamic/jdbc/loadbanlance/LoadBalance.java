package org.g2.starter.dynamic.jdbc.loadbanlance;

/**
 * @author wuwenxi 2023-02-10
 */
public interface LoadBalance<T> {

    /**
     * 负载key
     *
     * @return key
     */
    T choose();

    /**
     * 指定key
     *
     * @param index index
     * @return key
     */
    T choose(int index);

    /**
     * 负载范围
     *
     * @return int
     */
    int size();
}
