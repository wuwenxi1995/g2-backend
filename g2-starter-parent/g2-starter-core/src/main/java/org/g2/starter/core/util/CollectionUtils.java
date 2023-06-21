package org.g2.starter.core.util;

import java.util.*;

/**
 * @author wenxi.wu@hand-china.com 2020-11-12
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return null == collection || collection.size() == 0;
    }

    /**
     * 平均分成num个数组
     *
     * @param sourceList 数组
     * @param num        分解个数
     * @param <T>        数据类型
     * @return 分解结果
     */
    public static <T> List<List<T>> averageSplit(List<T> sourceList, int num) {
        if (isEmpty(sourceList) || num < 1) {
            return null;
        }
        List<List<T>> result = new ArrayList<>();
        int rest = sourceList.size() % num;
        int number = sourceList.size() / num;
        // 偏移量
        int offset = 0;
        for (int index = 0; index < num; index++) {
            List<T> source;
            if (rest > 0) {
                source = sourceList.subList(index * number + offset, (index + 1) * number + offset + 1);
                rest--;
                offset++;
            } else {
                source = sourceList.subList(index * number + offset, (index + 1) * number + offset);
            }
            result.add(source);
        }
        return result;
    }

    /**
     * 分解数组
     *
     * @param sourceList 数组
     * @param size       分解的数组大小
     * @param <T>        数组类型
     * @return 分解结果
     */
    public static <T> List<List<T>> splitList(List<T> sourceList, int size) {
        if (isEmpty(sourceList) || size < 1) {
            return null;
        }

        List<List<T>> result = new ArrayList<>();

        int num = (sourceList.size() + size - 1) / size;
        for (int index = 0; index < num; index++) {
            result.add(sourceList.subList(index * size, Math.min(sourceList.size(), (index + 1) * size)));
        }
        return result;
    }


    // =================================================================
    //          内容来源Thinking in java 第15章 泛型
    // =================================================================

    /**
     * 取并集
     *
     * @param a   集合a
     * @param b   集合b
     * @param <T> 泛型
     * @return 并集
     */
    public static <T> Set<T> union(Collection<T> a, Collection<T> b) {
        Set<T> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 取交集
     *
     * @param a   集合a
     * @param b   集合b
     * @param <T> 泛型
     * @return 交集
     */
    public static <T> Set<T> intersection(Collection<T> a, Collection<T> b) {
        Set<T> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    /**
     * 取差集
     *
     * @param superset 目标集合
     * @param subset   被移除的集合
     * @param <T>      泛型
     * @return 差集
     */
    public static <T> Set<T> difference(Collection<T> superset, Collection<T> subset) {
        Set<T> result = new HashSet<>(superset);
        result.removeAll(subset);
        return result;
    }

    /**
     * 取补集，用ab并集移除ab交集
     *
     * @param a   集合a
     * @param b   集合b
     * @param <T> 泛型
     * @return 补集
     */
    public static <T> Set<T> complement(Collection<T> a, Collection<T> b) {
        return difference(union(a, b), intersection(a, b));
    }
}
