package com.wwx.study.algorithms.code.链表.print;

import com.wwx.study.algorithms.code.链表.Node;

/**
 * 两个有序链表头指针head1和head2，打印链表的公共部分
 *
 * @author wuwenxi 2022-03-31
 */
public class PrintNode {

    public <T> void print(Node<T> head1, Node<T> head2) {
        Node<T> temp;
        while (head1 != null) {
            while (head2 != null) {
                temp = head2;
                head2 = head2.getNext();
                if (temp.getValue() == head1.getValue()) {
                    System.out.println(head1.getValue());
                    break;
                }
            }
            head1 = head1.getNext();
        }
    }
}
