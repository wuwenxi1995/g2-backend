package com.wwx.study.algorithms.practice.leetcode;

import com.wwx.study.algorithms.code.链表.Node;

/**
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 *  
 * <p>
 * 示例 1：
 * 输入：head = [1,2,3,4,5]
 * 输出：[5,4,3,2,1]
 * <p>
 * <p>
 * 示例 2：
 * 输入：head = [1,2]
 * 输出：[2,1]
 * <p>
 * 示例 3：
 * 输入：head = []
 * 输出：[]
 * <p>
 * 链表中节点的数目范围是 [0, 5000]
 * -5000 <= Node.val <= 5000
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-linked-list
 *
 * @author wuwenxi 2022-03-31
 */
public class ReverseNode {

    public <T> Node<T> reverseNode(Node<T> node) {
        if (node == null || node.getNext() == null) {
            return node;
        }
        Node<T> head = null;
        Node<T> curr = node;
        while (curr.getNext() != null) {
            Node<T> next = curr.getNext();
            // 当前节点下一节点设置为prev
            curr.getNext().setNext(head);
            //
            head = curr;
            curr = next;
        }
        return curr;
    }

    public <T> void reverseNode(Node.LinkedNode<T> node) {

    }
}
