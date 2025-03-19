package com.wwx.study.algorithms.code.树.practice;

import com.wwx.study.algorithms.code.树.Node;

/**
 * 将树序列化与反序列化，序列化与反序列化出的结果应该一致
 *
 * @author wuwenxi 2022-04-27
 */
public class SerializeTree {

    public static String serializeTree(Node head) {
        if (head == null) {
            return "#";
        }
        StringBuilder sb = new StringBuilder();
        serialize(head, sb);
        return sb.toString();
    }

    private static void serialize(Node node, StringBuilder sb) {
        if (node == null) {
            sb.append("#_");
            return;
        }
        sb.append(node.value).append("_");
        serialize(node.left, sb);
        serialize(node.right, sb);
    }

    public static Node deserializeTree(String str) {
        return null;
    }
}
