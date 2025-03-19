package org.g2.starter.core.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据处理工具类
 *
 * @author wuwenxi 2021-09-28
 */
public class DataUniqueUtil {

    private DataUniqueUtil() {
    }

    /**
     * 数据去重
     */
    public static <K, T> Map<K, T> filter(List<T> operationDataList, DataUniqueHandler<K, T> handler) {
        if (CollectionUtils.isEmpty(operationDataList)) {
            return null;
        }
        Map<K, T> result = new ConcurrentHashMap<>(operationDataList.size() >> 1);
        operationDataList.parallelStream()
                .filter(handler::filter)
                .collect(Collectors.groupingBy(handler::mapKey))
                .entrySet().parallelStream()
                .forEach((entry) -> {
                    T choose;
                    K key = entry.getKey();
                    List<T> value = entry.getValue();
                    if (value.size() > 1) {
                        // 选择要处理的数据
                        choose = handler.choose(value);
                        if (choose == null) {
                            return;
                        }
                        result.put(key, choose);
                        // 处理脏数据
                        List<T> filter = value.stream().filter(e -> !e.equals(choose)).collect(Collectors.toList());
                        handler.abandonData(filter);
                    } else {
                        choose = value.get(0);
                        result.put(key, choose);
                    }
                    // 处理已选择的数据
                    handler.handler(choose);
                });
        return result;
    }

    /**
     * 筛选重复元素
     */
    public static <K, T> List<K> filterDumpData(List<T> operationDatum, DumpDataHandler<K, T> dumpDataHandler) {
        return operationDatum.stream()
                .filter(dumpDataHandler::filter)
                .collect(Collectors.groupingBy(dumpDataHandler::mapKey))
                .entrySet().parallelStream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public interface DataUniqueHandler<K, T> {
        /**
         * 返回需要分组的健
         *
         * @param value 值
         * @return 健
         */
        K mapKey(T value);

        /**
         * 在分组数据中选择一个需要对数据
         *
         * @param operationData 分组数据
         * @return 选择
         */
        T choose(List<T> operationData);

        /**
         * 对无用的数据做处理
         *
         * @param abandonVar 无用数据
         */
        void abandonData(List<T> abandonVar);

        /**
         * 对已选择对数据做处理
         *
         * @param choose 已选择的数据
         */
        default void handler(T choose) {
        }

        /**
         * 需要过滤的数据
         *
         * @param value 值
         * @return true/false
         */
        default boolean filter(T value) {
            return true;
        }
    }

    public interface DumpDataHandler<K, T> {
        /**
         * 返回需要分组的健
         *
         * @param value 值
         * @return 健
         */
        K mapKey(T value);

        /**
         * 需要过滤的数据
         *
         * @param value 值
         * @return true/false
         */
        default boolean filter(T value) {
            return true;
        }
    }
}
