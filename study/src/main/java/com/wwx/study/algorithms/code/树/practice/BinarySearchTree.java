package com.wwx.study.algorithms.code.树.practice;

import com.wwx.study.algorithms.code.树.Node;

import java.util.LinkedList;

/**
 * 判断是否为搜索二叉树
 * <p>
 * 搜索二叉树：左树比根结点小，右树比根结点大
 * </p>
 *
 * @author wuwenxi 2022-04-05
 */
public class BinarySearchTree {

    private static int preValue = Integer.MIN_VALUE;

    /**
     * 中序遍历递归改进版
     */
    public static boolean isBst1(Node head) {
        if (head == null) {
            return true;
        }
        boolean isLeftBst = isBst1(head.left);
        if (!isLeftBst) {
            return false;
        }
        if (head.value <= preValue) {
            return false;
        }
        preValue = head.value;
        return isBst1(head.right);
    }

    public static boolean isBst2(Node node) {
        LinkedList<Node> stack = new LinkedList<>();
        int preValue = Integer.MIN_VALUE;
        while (!stack.isEmpty() || node != null) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else {
                Node head = stack.pop();
                if (head.value <= preValue) {
                    return false;
                }
                preValue = head.value;
                node = head.right;
            }
        }
        return true;
    }

    /**
     *
     */
    public static boolean isBst3(Node node) {
        return process(node).isSearch;
    }


    /**
     * 动态规划
     */
    private static ReturnType process(Node node) {
        if (node == null) {
            return null;
        }

        ReturnType left = process(node.left);
        ReturnType right = process(node.right);

        int min = node.value, max = node.value;
        boolean isSearch = ((left == null || (left.isSearch && left.max < node.value))
                && (right == null || (right.isSearch && node.value < right.min)));
        if (isSearch) {
            if (left != null) {
                min = Math.min(left.min, min);
            }
            if (right != null) {
                max = Math.max(right.max, max);
            }
        }
        return new ReturnType(isSearch, min, max);
    }

    private static class ReturnType {
        private boolean isSearch;
        private int min;
        private int max;

        ReturnType(boolean isSearch, int min, int max) {
            this.isSearch = isSearch;
            this.min = min;
            this.max = max;
        }
    }
}
