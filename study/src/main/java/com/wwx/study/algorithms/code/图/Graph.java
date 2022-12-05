package com.wwx.study.algorithms.code.图;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 图结构
 *
 * @author wuwenxi 2022-05-25
 */
public class Graph<T> {

    /**
     * 点集合,key-value:编号-点信息
     */
    Map<Integer, Node<T>> nodes;

    /**
     * 边集合
     */
    Set<Edge<T>> edges;

    public Graph() {
        nodes = new HashMap<>();
        edges = new HashSet<>();
    }

    public Map<Integer, Node<T>> getNodes() {
        return nodes;
    }

    public Set<Edge<T>> getEdges() {
        return edges;
    }
}
