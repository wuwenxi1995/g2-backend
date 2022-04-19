package org.g2.inv.domain.repository;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @author wuwenxi 2022-04-18
 */
public interface InvOperationStockRepository {

    /**
     * 读取服务点库存
     *
     * @param posCode  服务点编码
     * @param skuCodes sku编码
     * @return 库存信息
     */
    Map<String, Long> readPosStock(String posCode, Collection<String> skuCodes);

    /**
     * 读取服务点库存
     *
     * @param posCode         服务点编码
     * @param collectionT     数据集合T
     * @param skuCodeFunction 从T集合中返回skuCode集合
     * @param <T>             包含sku信息的数据类型
     * @return 服务点库存
     */
    <T> Map<String, Long> readPosStock(String posCode, Collection<T> collectionT, IFunction<T, String, String> skuCodeFunction);

    /**
     * 读取多个服务点的库存信息
     *
     * @param collectionK     数据集合K
     * @param collectionT     数据集合T
     * @param posCodeFunction 从K集合中返回posCode
     * @param skuCodeFunction 从T集合中返回skuCode集合
     * @param <K>             包含pos数据类型
     * @param <T>             包含sku信息的数据类型
     * @return 多个服务点库存信息
     */
    <K, T> Map<String, Map<String, Long>> readPosStock(Collection<K> collectionK, Collection<T> collectionT, Function<K, String> posCodeFunction, IFunction<T, String, String> skuCodeFunction);

    /**
     * 检查库存是否可用
     *
     * @param posCode                 服务点编码
     * @param collectionT             数据集合T
     * @param skuCodeQuantityFunction 返回sku需要查询的数据
     * @param skuCodeFunction         从T集合中返回skuCode集合
     * @param <T>                     包含sku信息的数据类型
     * @return true/false
     */
    <T> boolean checkStockEnough(String posCode, Collection<T> collectionT, Function<String, Long> skuCodeQuantityFunction, IFunction<T, String, String> skuCodeFunction);

    /**
     * 校验服务点库存
     *
     * @param collectionK             数据集合K
     * @param collectionT             数据集合T
     * @param posCodeFunction         从K集合中返回posCode
     * @param skuCodeQuantityFunction 返回sku需要查询的数据
     * @param skuCodeFunction         从T集合中返回skuCode集合
     * @param <K>                     包含pos数据类型
     * @param <T>                     包含sku信息的数据类型
     * @return 有库存的服务点
     */
    <K, T> Collection<K> checkStockEnough(Collection<K> collectionK, Collection<T> collectionT, Function<K, String> posCodeFunction, Function<String, Long> skuCodeQuantityFunction, IFunction<T, String, String> skuCodeFunction);


    @FunctionalInterface
    interface IFunction<K, T, V> {
        /**
         * 按条件筛选集合数据
         *
         * @param collection 数据
         * @param condition  条件
         * @return 集合
         */
        Collection<V> apply(Collection<K> collection, T condition);
    }
}
