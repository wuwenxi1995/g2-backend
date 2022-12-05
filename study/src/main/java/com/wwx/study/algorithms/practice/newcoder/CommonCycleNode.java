package com.wwx.study.algorithms.practice.newcoder;

/**
 * 输入两个可能有环的单向链表，找出它们的第一个公共结点，如果没有公共节点则返回空。
 *
 * <p>
 * 基本思路：
 * 1. 一个有环，一个没有环，一定不相交(原因：如果相交的话，相交节点之后一定存在环，所以)
 * 2. 两个链表都有环
 * 2.1 两个链表不相交
 * 2.2 两个链表相交，共用一个环，且入环点相同
 * 2.3 两个链表相交，共用一个环，但入环点不同
 * 3. 判断2中的所有情况
 * 3.1 如果两个入环点相等，则一定为2.2情况
 * 3.2 如果两个入环点不相等；从一个链表点入环点进行遍历，从入环点到入环点
 * 3.2.1 如果过程中没有遇到等于另一个入环点，则为2.1;
 * 3.2.2 如果遇到了另一个入环点，则为2.3
 * 4. 两个都没有环{@link CommonNode#findFirstCommonNode2(ListNode, ListNode)}
 * </p>
 *
 * @author wuwenxi 2022-04-01
 */
public class CommonCycleNode {

    public ListNode commonCycleNode(ListNode pHead1, ListNode pHead2) {
        if (pHead1 == null || pHead2 == null) {
            return null;
        }
        ListNode loopNode1 = CycleNode.isLoop(pHead1);
        ListNode loopNode2 = CycleNode.isLoop(pHead2);
        // 两个都不存在环
        if (loopNode1 == null && loopNode2 == null) {
            return CommonNode.findFirstCommonNode2(pHead1, pHead2);
        }
        // 一个有环一个无环
        if (loopNode1 == null || loopNode2 == null) {
            return null;
        }
        // 两个链表都有环
        // 如果入环点为同一个
        if (loopNode1 == loopNode2) {
            // 从两个链表头到入环点之间查询第一个共同节点
            // 相当于去掉两个链表的环
            ListNode node1 = pHead1, node2 = pHead2;
            while (node1 != null && node1 != loopNode1) {
                node1 = node1.next;
            }
            while (node2 != null && node2 != loopNode2) {
                node2 = node2.next;
            }
            return CommonNode.findFirstCommonNode2(node1, node2);
        }
        // 入环点不是同一个
        else {
            // 判断环是否相交；
            // 在链表1上从入环点旋转一周；如果遇到链表2入环点，则两个链表相交；否则不相交
            ListNode next = loopNode1.next;
            while (next != loopNode1) {
                // 如果有遇到等于loopNode2的节点，说明相交，返回两个入环点都对
                if (next == loopNode2) {
                    return loopNode1;
                }
                next = next.next;
            }
            // 如果没有有遇到等于loopNode2的节点，说明不相交
            return null;
        }
    }
}
