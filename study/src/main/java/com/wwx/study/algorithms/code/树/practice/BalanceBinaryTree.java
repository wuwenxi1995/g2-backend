package com.wwx.study.algorithms.code.树.practice;

import com.wwx.study.algorithms.code.树.Node;

/**
 * 是否为平衡二叉树
 *
 * <p>
 * 任何一个数的左子树与右子树高度差不超过1
 * 基本思路：
 * 1. 左右子树都是平衡的
 * 2. 左右子树高度差不超过1
 * </p>
 *
 * @author wuwenxi 2022-04-05
 */
public class BalanceBinaryTree {

    private static class ResultType {
        private boolean isBalance;
        private int height;

        ResultType(boolean isBalance, int height) {
            this.isBalance = isBalance;
            this.height = height;
        }
    }

    public static boolean isBalance(Node node) {
        return process(node).isBalance;
    }

    private static ResultType process(Node node) {
        if (node == null) {
            return new ResultType(true, 0);
        }

        ResultType left = process(node.left);
        ResultType right = process(node.right);

        int height = -1;
        boolean isBalance = left.isBalance && right.isBalance && (Math.abs(left.height - right.height) < 2);
        if (isBalance) {
            height = Math.max(left.height, right.height) + 1;
        }
        return new ResultType(isBalance, height);
    }
}
