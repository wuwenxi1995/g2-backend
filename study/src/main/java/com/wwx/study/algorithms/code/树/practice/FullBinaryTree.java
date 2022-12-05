package com.wwx.study.algorithms.code.树.practice;

import com.wwx.study.algorithms.code.树.Node;

import java.util.LinkedList;

/**
 * 判断是否为满二叉树
 *
 * <p>
 * 思路1：判断每一层结点数是否为2幂次
 * 思路2：判断整棵树结点数是否为2^n - 1
 * </p>
 *
 * @author wuwenxi 2022-04-05
 */
public class FullBinaryTree {

    public static boolean isFullBinaryTree(Node node) {
        if (node == null) {
            return true;
        }
        LinkedList<Node> queue = new LinkedList<>();
        queue.offer(node);
        // 记录遍历层数
        boolean first = true;
        while (!queue.isEmpty()) {
            int size = queue.size();
            if (first && (size & (size - 1)) != 0) {
                return false;
            }
            if (first) {
                first = false;
            }
            for (int i = 0; i < size; i++) {
                node = queue.peek();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return true;
    }

    /**
     * 树型DP
     */
    public static boolean isFullBinaryTree1(Node node) {
        if (node == null) {
            return true;
        }
        ReturnType result = process(node);
        return result.nodes == ((1 << result.height) - 1);
    }

    private static ReturnType process(Node node) {
        if (node == null) {
            return new ReturnType(0, 0);
        }

        ReturnType left = process(node.left);
        ReturnType right = process(node.right);

        int height = Math.max(left.height, right.height) + 1;
        int nodes = left.nodes + right.nodes + 1;
        return new ReturnType(height, nodes);
    }

    private static class ReturnType {
        private int height;
        private int nodes;

        ReturnType(int height, int nodes) {
            this.height = height;
            this.nodes = nodes;
        }
    }
}
