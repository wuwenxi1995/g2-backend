package org.g2.core.util;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wenxi.wu@hand-china.com 2020-11-12
 */
public class CollectionUtils {


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
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
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
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
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
    public static <T> Set<T> difference(Set<T> superset, Set<T> subset) {
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
    public static <T> Set<T> complement(Set<T> a, Set<T> b) {
        return difference(union(a, b), intersection(a, b));
    }
}
