package org.g2.core.util;

import java.util.Objects;

/**
 * Spring 提供的Pair没有默认的构造方法，导致jackson无法序列化
 *
 * @author wuwenxi 2021-07-07
 */
public class Pair<K, V> {

    private K first;
    private V second;

    public Pair() {
    }

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public static <K, V> Pair<K, V> of(K first, V second) {
        return new Pair<>(first, second);
    }

    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return Objects.equals(pair.first, first) &&
                Objects.equals(pair.second, second);
    }
}
