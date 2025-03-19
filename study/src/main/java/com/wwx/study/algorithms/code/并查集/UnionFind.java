package com.wwx.study.algorithms.code.并查集;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * @author wuwenxi 2022-08-01
 */
public class UnionFind<V> {

    /**
     * 存放值和节点
     */
    private Map<V, Element<V>> elementMap;
    /**
     * 存放子节点和根节点
     */
    private Map<Element<V>, Element<V>> parentMap;
    /**
     * 存放每个根节点下节点数
     */
    private Map<Element<V>, Integer> sizeMap;
    private LinkedList<Element<V>> stack;

    public UnionFind() {
        this.elementMap = new HashMap<>();
        this.parentMap = new HashMap<>();
        this.sizeMap = new HashMap<>();
        this.stack = new LinkedList<>();
    }

    public UnionFind(Collection<V> unionList) {
        this();
        for (V v : unionList) {
            Element<V> element = new Element<>(v);
            elementMap.put(v, element);
            parentMap.put(element, element);
            sizeMap.put(element, 1);
        }
    }

    public boolean isSameSet(V a, V b) {
        if (elementMap.containsKey(a) && elementMap.containsKey(b)) {
            return findHead(elementMap.get(a)) == findHead(elementMap.get(b));
        }
        return false;
    }

    public void union(V a, V b) {
        Element<V> eA, eB;
        if ((eA = elementMap.get(a)) != null && (eB = elementMap.get(b)) != null) {
            Element<V> aF = findHead(eA);
            Element<V> bF = findHead(eB);
            if (aF != bF) {
                Element<V> big = sizeMap.get(aF) >= sizeMap.get(bF) ? aF : bF;
                Element<V> small = aF == big ? bF : aF;
                // 合并
                parentMap.put(small, big);
                // 更新size
                sizeMap.put(big, sizeMap.get(aF) + sizeMap.get(bF));
                sizeMap.remove(small);
            }
        }
    }

    private Element<V> findHead(Element<V> element) {
        while (element != parentMap.get(element)) {
            stack.push(element);
            element = parentMap.get(element);
        }
        // 将结构扁平化, 将所有节点父节点均设置为elemnet
        while (!stack.isEmpty()) {
            parentMap.put(stack.pop(), element);
        }
        return element;
    }

    private static class Element<V> {

        private V value;

        Element(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Element<?> element = (Element<?>) o;
            return Objects.equals(value, element.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}