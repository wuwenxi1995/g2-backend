package org.g2.core.util;

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

    public static <K, T> Map<K, T> filter(List<T> operationDataList, DataUniqueHandler<K, T> handler) {
        if (CollectionUtils.isEmpty(operationDataList)) {
            return null;
        }
        boolean parallel = handler.parallel();
        Stream<T> stream = parallel ? operationDataList.parallelStream() : operationDataList.stream();
        Map<K, List<T>> groupMap = stream.collect(Collectors.groupingBy(handler::mapKey));
        Map<K, T> result;
        if (parallel && groupMap.size() > handler.batchSize()) {
            ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
            result = new ConcurrentHashMap<>(groupMap.size() << 1);
            ParallelTask<K, T> task = new ParallelTask<>(new ArrayList<>(groupMap.values()), result, handler);
            forkJoinPool.submit(task);
        } else {
            result = new HashMap<>(groupMap.size() << 1);
            groupMap.forEach((key, value) -> handler(key, value, result, handler));
        }
        return result;
    }

    public static <K, T> void filterAndHandler(List<T> operationDataList, DataUniqueHandler<K, T> handler) {
        if (CollectionUtils.isEmpty(operationDataList)) {
            return;
        }
        boolean parallel = handler.parallel();
        Stream<T> stream = parallel ? operationDataList.parallelStream() : operationDataList.stream();
        Map<K, List<T>> groupMap = stream.collect(Collectors.groupingBy(handler::mapKey));
        if (parallel && groupMap.size() > handler.batchSize()) {
            ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
            ParallelTask<K, T> task = new ParallelTask<>(new ArrayList<>(groupMap.values()), handler);
            forkJoinPool.submit(task);
        } else {
            groupMap.forEach((key, value) -> handler(value, handler));
        }
    }

    private static class ParallelTask<K, T> extends RecursiveAction {

        private List<List<T>> operationDataList;
        private Map<K, T> result;
        private DataUniqueHandler<K, T> handler;

        ParallelTask(List<List<T>> operationDataList, Map<K, T> result, DataUniqueHandler<K, T> handler) {
            this(operationDataList, handler);
            this.result = result;
        }

        ParallelTask(List<List<T>> operationDataList, DataUniqueHandler<K, T> handler) {
            this.operationDataList = operationDataList;
            this.handler = handler;
        }

        @Override
        protected void compute() {
            int batchSize = handler.batchSize();
            if (operationDataList.size() < batchSize) {
                for (List<T> value : operationDataList) {
                    if (result != null) {
                        handler(handler.mapKey(value.get(0)), value, result, handler);
                    } else {
                        handler(value, handler);
                    }
                }
            } else {
                int index = operationDataList.size() >> 1;
                ParallelTask<K, T> left = new ParallelTask<>(operationDataList.subList(0, index), result, handler);
                ParallelTask<K, T> right = new ParallelTask<>(operationDataList.subList(index + 1, operationDataList.size()), result, handler);
                invokeAll(left, right);
            }
        }
    }

    private static <K, T> void handler(K key, List<T> value, Map<K, T> result, DataUniqueHandler<K, T> handler) {
        if (value.size() > 1) {
            // 选择要处理的数据
            T choose = handler.choose(value);
            if (choose == null) {
                return;
            }
            result.put(key, choose);
            // 处理脏数据
            List<T> filter = value.stream().filter(e -> !e.equals(choose)).collect(Collectors.toList());
            handler.abandonData(filter);
            // 处理已选择的数据
            handler.handler(choose);
        } else {
            result.put(key, value.get(0));
        }
    }

    private static <K, T> void handler(List<T> value, DataUniqueHandler<K, T> handler) {
        T choose;
        if (value.size() > 1) {
            // 选择要处理的数据
            choose = handler.choose(value);
            if (choose == null) {
                return;
            }
            // 处理脏数据
            List<T> filter = value.stream().filter(e -> !e.equals(choose)).collect(Collectors.toList());
            handler.abandonData(filter);
        } else if (value.size() == 1) {
            choose = value.get(0);
        } else {
            choose = null;
        }
        if (choose != null) {
            // 处理已选择的数据
            handler.handler(choose);
        }
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
         * 是否执行并行任务
         *
         * @return true
         */
        default boolean parallel() {
            return true;
        }

        /**
         * 批数据量
         *
         * @return int
         */
        default int batchSize() {
            return 1000;
        }
    }
}
