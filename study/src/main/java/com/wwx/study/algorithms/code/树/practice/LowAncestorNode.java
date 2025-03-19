package com.wwx.study.algorithms.code.树.practice;

import com.wwx.study.algorithms.code.树.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 寻找两个结点最低公共祖先
 * <p>
 * 最低公共祖先：两个结点向上寻找第一个交汇的结点
 * </p>
 *
 * @author wuwenxi 2022-04-22
 */
public class LowAncestorNode {

    /**
     * 思路1：
     * 1. 将所有结点与其父结点存入map中
     * 2. 将其中一个查询的结点，从这个结点到根结点全部存入set中
     * 3. 找到另一个结点到根结点的过程中与第一个结点的集合重合的结点，为最低公共祖先
     */
    public static Node lan1(Node head, Node o1, Node o2) {
        if (head == null) {
            return null;
        }
        // 存放当前结点与其父结点
        Map<Node, Node> map = new HashMap<>();
        map.put(head, head);
        process(head, map);
        // 记录o1到根结点的全部结点
        Set<Node> list = new HashSet<>();
        Node cur = o1;
        while (cur != map.get(cur)) {
            list.add(cur);
            cur = map.get(cur);
        }
        // 从o2到根结点，找寻与list中重合的结点
        Node temp = o2;
        while (true) {
            if (list.contains(temp)) {
                return temp;
            }
            temp = map.get(temp);
        }
    }

    private static void process(Node node, Map<Node, Node> map) {
        if (node == null) {
            return;
        }
        map.put(node.left, node);
        map.put(node.right, node);
        process(node.left, map);
        process(node.right, map);
    }

    /**
     * 思路2：
     * 1. o1是o2的最低祖先结点或 o2是o1的最低祖先结点; 遇到o1或o2则直接返回
     * 2. o1和o2不互为最低祖先结点，需要通过向上寻找结点才能确定祖先结点，所以祖先结点的左右一定不返回空
     */
    public static Node lan2(Node head, Node o1, Node o2) {
        // head为空直接返回空或递归过程中遇到o1或o2则直接返回o1/o2
        if (head == null || head == o1 || head == o2) {
            return head;
        }
        // 如果是情况1，在递归之后则左右子树一定只有一个返回
        // 如果是情况2，在递归之后则左右子树都有返回
        Node left = lan2(head.left, o1, o2);
        Node right = lan2(head.right, o1, o2);
        // 如果左右递归结果都不为空，则当前结点为祖先结点
        if (left != null && right != null) {
            return head;
        }
        // 左右子树，并不都有值
        return left != null ? left : right;
    }
}
