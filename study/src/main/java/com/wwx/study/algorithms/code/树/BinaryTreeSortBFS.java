package com.wwx.study.algorithms.code.树;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉遍历，广度优先
 *
 * @author wuwenxi 2022-03-23
 */
public class BinaryTreeSortBFS {

    public static void main(String[] args) {
        Node root = new Node(3);
        Node rootLeft = new Node(9);
        Node rootRight = new Node(25);
        Node rootRightLeft = new Node(11);
        Node rootRightRight = new Node(18);
        root.left = rootLeft;
        root.right = rootRight;
        rootRight.left = rootRightLeft;
        rootRight.right = rootRightRight;
        bfs(root);
    }

    /**
     * 入队，广度优先，采用队列的形式
     * 循环遍历队列中是否有值，
     * 先将头结点入队，保存结点值
     * 判断节点是否有子结点，如果有子结点，将子树加入队列尾部
     * 依次类推，直到队列为空
     */
    private static void bfs(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
//            Node node = queue.poll();
//            System.out.print(node.value+" ");
//            if (node.left != null) {
//                queue.offer(node.left);
//            }
//            if (node.right != null) {
//                queue.offer(node.right);
//            }
            List<Integer> data = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                data.add(node.value);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            result.add(data);
        }
        System.out.println(result.toString());
    }
}
