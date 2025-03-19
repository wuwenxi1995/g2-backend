package com.wwx.study.algorithms.code.树;

import java.util.Objects;

/**
 * @author wuwenxi 2022-04-02
 */
public class Node {
    public int value;
    public Node left;
    public Node right;
    public Node parent;

    public Node(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        Node node = (Node) obj;
        return this.value == node.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, left, right);
    }
}
