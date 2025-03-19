package com.wwx.study.algorithms.code.链表.group;

import com.wwx.study.algorithms.code.链表.Node;

/**
 * 给定一个链表Node，给定一个值x，将链表中小于x的节点放左边，大于x的节点放右边
 *
 * <p>
 * 思路1：创建一个Node[]数组，做快速排序，最后将数组中的Node顺序重新连接
 * </p>
 *
 * <p>
 * 思路2：定义6个点，两个保存小于x的node节点头和尾，两个保存等于x的node节点的头和尾，两个保存大于node节点的头和尾，最后将头尾相连
 * </p>
 *
 * @author wuwenxi 2022-04-01
 */
public class GroupNode {

    public <T> Node<T> group1(Node<T> head) {
        return null;
    }

    /**
     * 分段记录各个范围的节点，最后再连接起来
     */
    public Node<Integer> group2(Node<Integer> head, int x) {
        if (head == null) {
            return null;
        }
        Node<Integer> sHead = null, sTail = null, mHead = null, mTail = null, lHead = null, lTail = null, temp;
        while (head != null) {
            temp = head;
            temp.setNext(null);
            if (temp.getValue() < x) {
                if (sHead == null) {
                    sHead = temp;
                } else {
                    sTail.setNext(temp);
                }
                sTail = temp;
            } else if (temp.getValue() > x) {
                if (lHead == null) {
                    lHead = temp;
                } else {
                    lTail.setNext(temp);
                }
                lTail = temp;
            } else {
                if (mHead == null) {
                    mHead = temp;
                } else {
                    mTail.setNext(temp);
                }
                mTail = temp;
            }
            head = head.getNext();
        }
        // 将存在的链表相连
        if (sTail != null) {
            sTail.setNext(mTail != null ? mTail.setNext(lHead) : lHead);
            return sHead;
        }
        if (mTail != null) {
            mTail.setNext(lHead);
            return mHead;
        }
        return lHead;
    }
}
