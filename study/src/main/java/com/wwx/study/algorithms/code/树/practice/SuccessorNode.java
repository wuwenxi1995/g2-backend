package com.wwx.study.algorithms.code.树.practice;

import com.wwx.study.algorithms.code.树.Node;

/**
 * 求某一结点n的后继结点(中序遍历)，结点有父结点信息，要求时间复杂度O(X), X为结点中序遍历的位置
 *
 * <p>
 * 思路：
 * 1. 如果n结点有右子树，则该右子树的最左结点一定为后继结点，如果右子树不存在左子树，则右子树为后继结点
 * 2. 如果n结点不存在右子树，则向上寻找祖先结点左子树不为空的结点，该祖先结点为n结点后续结点，如果祖先结点为根结点，则n结点的后续结点为空
 * </p>
 *
 * @author wuwenxi 2022-04-22
 */
public class SuccessorNode {

    public static Node successor(Node n) {
        if (n == null) {
            return null;
        }
        if (n.right != null) {
            Node result = n.right;
            while (result.left != null) {
                result = result.left;

            }
            return result;
        } else {
            Node parent = n.parent;
            while (parent != null && parent.left != n) {
                n = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }
}
