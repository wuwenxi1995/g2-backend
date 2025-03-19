package com.wwx.study.algorithms.code.树.practice;

import com.wwx.study.algorithms.code.树.Node;

import java.util.LinkedList;

/**
 * 判断是否为完全二叉树
 *
 * <p>
 * 完全二叉树：每一层结点都是从左到右依次排列
 * </p>
 * <p>
 * 基本思路：
 * 1. 结点有右结点必须有左结点
 * 2. 满足1的情况下，遇到第一个子树不满的结点(如没有左右子树，或只有左子树)，
 * 这个结点后面的结点都必须为叶子结点
 * </p>
 *
 * @author wuwenxi 2022-04-07
 */
public class CompleteBinaryTree {

    public boolean isCbt(Node node) {
        if (node == null) {
            return false;
        }
        LinkedList<Node> queue = new LinkedList<>();
        queue.offer(node);
        // 出现叶子结点标识
        boolean leaf = false;
        while (!queue.isEmpty()) {
            node = queue.peek();
            // 不满足条件1或不满足条件2
            if ((node.left == null && node.right != null)
                    || (leaf && (node.left != null))) {
                return false;
            }
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            } else if (!leaf) {
                leaf = true;
            }
        }
        return true;
    }
}
