package com.wwx.study.algorithms.code.图.practice;

import com.wwx.study.algorithms.code.图.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 图--遍历
 *
 * @author wuwenxi 2022-05-30
 */
public class CodeGraphSearch {

    /**
     * 广度优先
     */
    public static <T> void bfs(Node<T> node) {
        if (node == null) {
            return;
        }
        LinkedList<Node<T>> queue = new LinkedList<>();
        Set<Node<T>> container = new HashSet<>();
        queue.offer(node);
        container.add(node);
        while (!queue.isEmpty()) {
            Node<T> cur = queue.poll();
            System.out.println(cur);
            for (Node<T> next : cur.getNexts()) {
                if (!container.contains(next)) {
                    container.add(next);
                    queue.add(next);
                }
            }
        }
    }

    /**
     * 深度优先
     */
    public static <T> void dfs(Node<T> node) {
        if (node == null) {
            return;
        }
        LinkedList<Node<T>> stack = new LinkedList<>();
        Set<Node<T>> container = new HashSet<>();
        stack.add(node);
        container.add(node);
        System.out.println(node);
        while (!stack.isEmpty()) {
            Node<T> cur = stack.pop();
            for (Node<T> next : cur.getNexts()) {
                if (!container.contains(next)) {
                    stack.add(cur);
                    stack.add(next);
                    container.add(next);
                    System.out.println(next);
                    break;
                }
            }
        }
    }

}
