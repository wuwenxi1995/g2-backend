package com.wwx.study.algorithms.code.图;


import java.util.Objects;

/**
 * @author wuwenxi 2022-05-25
 */
public class Edge<T> {

    /**
     * 权值
     */
    private int weight;
    /**
     * 记录点到点
     */
    private Node<T> from;
    private Node<T> to;

    public Edge(int weight, Node<T> from, Node<T> to) {
        this.weight = weight;
        this.from = from;
        this.to = to;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Node<T> getFrom() {
        return from;
    }

    public void setFrom(Node<T> from) {
        this.from = from;
    }

    public Node<T> getTo() {
        return to;
    }

    public void setTo(Node<T> to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Edge<?> edge = (Edge<?>) object;
        return weight == (edge.weight) &&
                Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, from, to);
    }
}
