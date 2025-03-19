package com.wwx.study.algorithms.code.链表;

/**
 * @author wuwenxi 2022-03-31
 */
public class Node<T> {

    private T value;
    private Node<T> next;

    Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public Node<T> setNext(Node<T> next) {
        this.next = next;
        return this;
    }

    public static class LinkedNode<T> extends Node<T> {

        private LinkedNode<T> next;
        private LinkedNode<T> prev;

        LinkedNode(T value) {
            super(value);
        }

        @Override
        public LinkedNode<T> getNext() {
            return next;
        }

        public void setNext(LinkedNode<T> next) {
            this.next = next;
        }

        public LinkedNode<T> getPrev() {
            return prev;
        }

        public void setPrev(LinkedNode<T> prev) {
            this.prev = prev;
        }
    }
}
