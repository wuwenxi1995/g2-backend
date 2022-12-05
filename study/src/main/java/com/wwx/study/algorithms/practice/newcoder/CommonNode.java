package com.wwx.study.algorithms.practice.newcoder;

import java.util.LinkedList;

/**
 * 输入两个无环的单向链表，找出它们的第一个公共结点，如果没有公共节点则返回空。
 *
 * <p>
 * https://www.nowcoder.com/practice/6ab1d9a29e88450685099d45c9e31e46
 * <p>
 * https://leetcode-cn.com/problems/intersection-of-two-linked-lists-lcci/
 *
 * <p>
 * 思路1：
 * 1. 将两个链表压入栈内
 * 2. 弹出第一个节点，如果不相等，说明两个链表一定没有交点
 * 3. 如果弹的第一个节点相等，则继续弹出节点，并记录上一次弹出的节点
 * 4. 直到某一次弹出的节点不等，则上一次弹出节点一定是交点
 * </p>
 *
 * <p>
 * 思路2：
 * 1.遍历两个链表
 * 2.记录两个链表的长度，并记录长度差值
 * 3.判断两个链表最后一个节点是否相等；如果不相等，说明两个链表一定没有交点
 * 4.如果相等，将长链表按照长度差值先走到长度相等的位置，然后再进行等长度的遍历
 * </p>
 *
 * @author wuwenxi 2022-04-01
 */
public class CommonNode {

    public ListNode findFirstCommonNode1(ListNode pHead1, ListNode pHead2) {
        if (pHead1 == null || pHead2 == null) {
            return null;
        }
        LinkedList<ListNode> stack1 = new LinkedList<>();
        LinkedList<ListNode> stack2 = new LinkedList<>();
        while (pHead1 != null) {
            stack1.push(pHead1);
            pHead1 = pHead1.next;
        }
        while (pHead2 != null) {
            stack2.push(pHead2);
            pHead2 = pHead2.next;
        }
        ListNode prev = null, pop1, pop2;
        while (!stack1.isEmpty() && !stack2.isEmpty()) {
            pop1 = stack1.pop();
            pop2 = stack2.pop();
            if (pop1 != pop2) {
                break;
            }
            prev = pop1;
        }
        return prev;
    }

    public static ListNode findFirstCommonNode2(ListNode pHead1, ListNode pHead2) {
        if (pHead1 == null || pHead2 == null) {
            return null;
        }
        // 找到两个链表最后一个节点
        // 记录两个链表长度差值
        ListNode p1 = pHead1, p2 = pHead2;
        int len = 0;
        while (p1.next != null) {
            len++;
            p1 = p1.next;
        }
        while (p2.next != null) {
            len--;
            p2 = p2.next;
        }
        // 如果两个链表最后一个节点不等，则链表不相交
        if (p1 != p2) {
            return null;
        }
        // p1指针始终指向长链表
        p1 = len > 0 ? pHead1 : pHead2;
        // p2指针指向短链表
        p2 = p1 == pHead1 ? pHead2 : pHead1;
        len = Math.abs(len);
        while (len != 0 && p1 != null) {
            len--;
            p1 = p1.next;
        }
        while (p1 != p2 && p1 != null && p2 != null) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return p1;
    }

    public ListNode findFirstCommonNode3(ListNode pHead1, ListNode pHead2) {
        if (pHead1 == null || pHead2 == null) {
            return null;
        }
        ListNode p1, p2, next = pHead1;
        int len1 = 0, len2 = 0;
        while (next.next != null) {
            len1++;
            next = next.next;
        }
        p1 = next;
        next = pHead2;
        while (next.next != null) {
            len2++;
            next = next.next;
        }
        p2 = next;
        if (p1 != p2) {
            return null;
        }
        p1 = pHead1;
        p2 = pHead2;
        if (len1 - len2 > 0) {
            for (int i = 0; i < len1 - len2 && p1 != null; i++) {
                p1 = p1.next;
            }

        } else if (len1 - len2 < 0) {
            for (int i = 0; i < len2 - len1 && p2 != null; i++) {
                p2 = p2.next;
            }
        }
        while (p1 != null && p2 != null) {
            if (p1 == p2) {
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        return p1;
    }
}
