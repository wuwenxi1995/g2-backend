package org.g2.starter.dynamic.jdbc.loadbanlance;

/**
 * @author wuwenxi 2023-02-10
 */
public interface LoadBalanceBuildFactory {

    /**
     * 创建负载工厂
     *
     * @param keys key
     * @return loadBalance
     */
    LoadBalance create(String[] keys);
}
