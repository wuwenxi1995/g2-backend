package com.wwx.study.algorithms.practice.newcoder;

/**
 * 给一个长度为n链表，若其中包含环，请找出该链表的环的入口结点，否则，返回null。
 * <p>
 * 数据范围： n\le10000n≤10000，1<=结点值<=100001<=结点值<=10000
 * 要求：空间复杂度 O(1)O(1)，时间复杂度 O(n)O(n)
 * <p>
 * https://www.nowcoder.com/practice/253d2c59ec3e4bc68da16833f79a38e4
 *
 * @author wuwenxi 2022-04-01
 */
public class CycleNode {

    public static ListNode isLoop(ListNode pHead) {
        if (pHead == null || pHead.next == null || pHead.next.next == null) {
            return null;
        }
        // 慢指针
        ListNode h1 = pHead.next;
        // 快指针
        ListNode h2 = pHead.next.next;
        // 如果h1 == h2 说明形成了环，结束循环
        while (h1 != h2) {
            // 无环
            if (h2.next == null || h2.next.next == null) {
                return null;
            }
            h1 = h1.next;
            h2 = h2.next.next;
        }
        // 快指针重新指向头节点
        h2 = pHead;
        // 快慢指针一次都走一步
        while (h1 != h2) {
            h1 = h1.next;
            h2 = h2.next;
        }
        return h1;
    }
}
