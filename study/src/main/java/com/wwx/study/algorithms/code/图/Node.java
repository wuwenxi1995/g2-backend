package com.wwx.study.algorithms.code.图;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wuwenxi 2022-05-25
 */
public class Node<T> {
    /**
     * 结点实际值
     */
    private T value;
    /**
     * 入度：其他结点指向当前结点数
     */
    int in;
    /**
     * 出度：当前结点指向其他结点数
     */
    int out;
    /**
     * 由当前结点出度的结点集合
     */
    List<Node<T>> nexts;
    /**
     * 当前结点入度和出度的边
     */
    List<Edge<T>> edges;

    public Node(T value) {
        this.value = value;
        this.in = 0;
        this.out = 0;
        this.nexts = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public T getValue() {
        return value;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    public List<Node<T>> getNexts() {
        return nexts;
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Node<?> node = (Node<?>) object;
        return in == node.in &&
                out == node.out &&
                Objects.equals(value, node.value) &&
                Objects.equals(nexts, node.nexts) &&
                Objects.equals(edges, node.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, in, out, nexts, edges);
    }
}
