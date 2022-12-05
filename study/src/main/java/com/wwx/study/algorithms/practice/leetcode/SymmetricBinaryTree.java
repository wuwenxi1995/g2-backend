package com.wwx.study.algorithms.practice.leetcode;

import com.wwx.study.algorithms.code.树.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 判断是否为对称二叉树
 *
 * @author wuwenxi 2022-03-24
 */
public class SymmetricBinaryTree {

    private boolean isSymmetric(Node node) {
        if (node == null) {
            return true;
        }
        return useQueue(node);
    }

    /**
     * 使用队列
     * 基本思路：
     * 1. 层级遍历
     * 2. 每一层结点从左到右加入到数组中
     * 3. 校验数组是否对称
     */
    private boolean useQueue(Node node) {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(node);
        List<Node> data = new ArrayList<>();
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size > 0) {
                Node first = queue.poll();
                data.add(first);
                queue.offer(first.left);
                queue.offer(first.right);
                --size;
            }
            int i = 0, j = data.size() - 1;
            while (i < j) {
                Node left = data.get(i);
                Node right = data.get(j);
                if (left != null || right != null) {
                    if (left == null || !left.equals(right)) {
                        return false;
                    }
                }
                i++;
                j--;
            }
            data.clear();
        }
        return false;
    }

    /**
     * 使用递归
     * 基本思路：
     * 1.左右结点如果都为空，返回true
     * 2.左结点 为空或左结点不等于右结点，返回false
     * 3.递归执行1、2，判断左结点的左子树与右结点的右子树相等并且左结点的右子树与右结点的左子树相等
     */
    private static boolean useRecursion(Node left, Node right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || !left.equals(right)) {
            return false;
        }
        // 判断左右子树是否对称
        return useRecursion(left.left, right.right) && useRecursion(left.right, right.left);
    }
}
