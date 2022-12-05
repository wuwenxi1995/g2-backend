package com.wwx.study.algorithms.code.链表.palindromic;

import com.wwx.study.algorithms.code.链表.Node;

import java.util.LinkedList;

/**
 * 判断链表是否回文，回文指整个链表是否对称；假设链表长度n，要求时间复杂度O(N)，空间复杂度O(1)
 * <p>
 * 如：1 -> 2 -> 3 -> 2 -> 1
 * 输出：true
 * 如：1 -> 2 -> 3 -> 1
 * 输出：false
 * 如：如：1 -> 2 -> 2 -> 1
 * 输出：true
 *
 * <p>
 * 思路1：遍历链表，将链表值压入栈中；再从栈中依次弹出值，与链表做对比
 * <p>
 * <p>
 * 思路2：快慢指针，定义两个指针，快指针走两步，慢指针走一步；当快指针走到链表尾部，慢指针一定走到链表中间位置；
 * 找到链表中间位置；1 -> 2 -> 3 <- 2 <- 1
 * 慢指针停留位置：
 * 1. 如果需要停留在中点位置，则慢指针初始位置在头节点
 * 2. 如果需要停留在中点位置后一个节点，则慢指针初始位置在头节点下一个节点
 * 3. 如果需要停留在中点位置前一个节点，则慢指针初始位置为null
 * </p>
 *
 * @author wuwenxi 2022-03-31
 */
public class PalindromicNode {

    /**
     * 普通方法：出栈入栈；
     */
    public <T> boolean isPalindrome(Node<T> node) {
        if (node == null || node.getNext() == null) {
            return true;
        }
        LinkedList<Node<T>> stack = new LinkedList<>();
        Node<T> head = node;
        while (head != null) {
            stack.push(head);
            head = head.getNext();
        }
        while (node != null) {
            if (node.getValue() != stack.pop().getValue()) {
                return false;
            }
            node = node.getNext();
        }
        return true;
    }

    /**
     * 改进方法：将链表中点之后点值压入栈内
     */
    public <T> boolean isPalindrome1(Node<T> node) {
        if (node == null || node.getNext() == null) {
            return true;
        }
        // 定义快慢指针
        Node<T> right = node.getNext(), cur = node;
        while (cur.getNext() != null && cur.getNext().getNext() != null) {
            right = right.getNext();
            cur = cur.getNext().getNext();
        }
        // 直将中点位置之后的节点压入栈内
        LinkedList<Node<T>> stack = new LinkedList<>();
        while (right != null) {
            stack.push(right);
            right = right.getNext();
        }
        // 从头遍历节点，与出栈节点对比
        while (!stack.isEmpty()) {
            if (node.getValue() != stack.pop().getValue()) {
                return false;
            }
            node = node.getNext();
        }
        return true;
    }

    /**
     * 改进方法：不使用栈，反转中点位置后的链表，与前面的节点做对比；空间复杂度为O(N)
     */
    public <T> boolean isPalindrome2(Node<T> node) {
        if (node == null || node.getNext() == null) {
            return true;
        }
        // n1 指针停在链表中点
        Node<T> n1 = node, n2 = node, n3;
        while (n2.getNext() != null && n2.getNext().getNext() != null) {
            n1 = n1.getNext();
            n2 = n2.getNext().getNext();
        }
        // 将n下一个指向置为null
        // 从n1下一个节点开始反转链表
        n2 = n1.getNext();
        n1.getNext().setNext(null);
        while (n2 != null) {
            n3 = n2.getNext();
            n2.setNext(n1);
            n1 = n2;
            n2 = n3;
        }
        // 此时n1为反转后的链表：==> 1->2->3<-2<-1
        n3 = n1;
        n2 = node;
        // 从头开始遍历
        boolean res = true;
        while (n1 != null && n2 != null) {
            if (n1.getValue() != n2.getValue()) {
                res = false;
                break;
            }
            n1 = n1.getNext();
            n2 = n2.getNext();
        }
        // 将反转的链表改回去
        // 为什么需要反转回去？
        n1 = n3.getNext();
        n3.getNext().setNext(null);
        while (n1 != null) {
            n2 = n1.getNext();
            n1.setNext(n3);
            n3 = n1;
            n1 = n2;
        }
        return res;
    }
}
