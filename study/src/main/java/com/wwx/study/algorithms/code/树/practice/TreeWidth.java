package com.wwx.study.algorithms.code.树.practice;

import com.wwx.study.algorithms.code.树.Node;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 求树的宽度最大宽度
 *
 * @author wuwenxi 2022-04-05
 */
public class TreeWidth {

    public static int treeWidth(Node node) {
        if (node == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        int max = Integer.MIN_VALUE;
        queue.offer(node);
        while (!queue.isEmpty()) {
            int size = queue.size();
            max = Math.max(max, size);
            for (int i = 0; i < size; i++) {
                Node poll = queue.poll();
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
                }
            }
        }
        return max;
    }
}
