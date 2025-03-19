package com.wwx.study.algorithms.code.树;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 二叉遍历，广度优先
 *
 * @author wuwenxi 2022-03-23
 */
public class BinaryTreeSortDFS {

    public static void main(String[] args) {
        Node root = new Node(7);
        Node left = new Node(3);
        Node leftleft = new Node(1);
        Node leftRight = new Node(2);
        left.left = leftleft;
        left.right = leftRight;
        Node right = new Node(6);
        Node rightleft = new Node(4);
        Node rightRight = new Node(5);
        right.left = rightleft;
        right.right = rightRight;
        root.left = left;
        root.right = right;
//        List<Integer> result = new ArrayList<>();
//        preOrderWithRecursion(root, result);
//        System.out.println(result.toString());
        // preOrder(root);
//        System.out.println();
//        inOrderWithRecursion(root);
//        System.out.println();
//        inOrder(root);
        // System.out.println();
        afterOrderWithRecursion(root);
        System.out.println();
        afterOrder(root);
    }

    /**
     * 二叉遍历，先序遍历；先记录根结点，再记录左结点，最后记录右结点
     * 通过入栈出栈方式解决：根结点先入栈，再将根结点左子树入栈，从出栈的结点找到右子树
     * 1. 将头结点压入栈内
     * 2. 弹出栈内第一个结点
     * 3. 打印结点值
     * 4. 先将结点右子树压入栈内，再将结点左子树压入站内
     * 5. 循环指向2、3、4步，直到栈为空
     */
    public static void preOrder1(Node node) {
        List<Integer> data = new ArrayList<>();
        LinkedList<Node> stack = new LinkedList<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            Node first = stack.pop();
            data.add(first.value);
            if (first.right != null) {
                stack.push(first.right);
            }
            if (first.left != null) {
                stack.push(first.left);
            }
        }
        System.out.println(data.toString());
    }

    public static void preOrder2(Node node) {
        List<Integer> data = new ArrayList<>();
        LinkedList<Node> stack = new LinkedList<>();
        stack.push(node);
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                data.add(node.value);
                stack.addFirst(node);
                node = node.left;
            }
            if (!stack.isEmpty()) {
                node = stack.removeFirst();
                node = node.right;
            }
        }
        System.out.println(data.toString());
    }

    /**
     * 递归
     */
    public static void preOrderWithRecursion(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }
        result.add(node.value);
        preOrderWithRecursion(node.left, result);
        preOrderWithRecursion(node.right, result);
    }

    /**
     * 二叉遍历，中序遍历；先记录左结点，再记录根结点，最后记录右结点
     * 通过入栈出栈方式解决：根结点先入栈，再将根结点左子树入栈，从出栈的结点找到右子树
     * 1. 将根结点先入栈
     * 2. 循环遍历找到当前结点的左结点，并入栈，直到左结点为空
     * 3. 出栈第一个，将结点加入list
     * 3.1 找寻出栈结点是否有右结点，及右结点的左结点
     * 3.2 执行第2步
     * 4. 继续执行第3步，直到结点为空或栈没有结点后结束
     */
    public static void inOrder(Node node) {
        if (node == null) {
            return;
        }
        List<Integer> data = new ArrayList<>();
        LinkedList<Node> stack = new LinkedList<>();
        while (!stack.isEmpty() || node != null) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else {
                Node first = stack.pop();
                data.add(first.value);
                node = first.right;
            }
        }
        System.out.println(data.toString());
    }

    /**
     * 递归
     */
    public static void inOrderWithRecursion(Node node) {
        if (node == null) {
            return;
        }
        inOrderWithRecursion(node.left);
        System.out.print(node.value + ",");
        inOrderWithRecursion(node.right);
    }

    /**
     * 压入收集栈的顺序：头->右->左；最终从收集栈中出来的顺序：左->右->头
     * 1. 准备两个栈，一个栈s1负责压入树的结点，一个栈s2负责收集从s1出栈的结点
     * 2. 结点s1从栈后，先压入s2栈中，
     * 3. 在将从s1出栈的结点的左右子树压入s1中，按照先压入左，再压入右的顺序
     * 4. 重复2、3操作，直到s1栈为空
     * 5. 打印s2栈
     */
    public static void afterOrder(Node node) {
        // 压入栈
        LinkedList<Node> stack1 = new LinkedList<>();
        // 收集栈
        LinkedList<Node> stack2 = new LinkedList<>();
        stack1.addFirst(node);
        while (!stack1.isEmpty()) {
            Node first = stack1.pop();
            stack2.push(first);
            if (first.left != null) {
                stack1.push(first.left);
            }
            if (first.right != null) {
                stack1.push(first.right);
            }
        }
        // 打印收集栈
        while (!stack2.isEmpty()) {
            System.out.print(stack2.pop().value + ",");
        }
    }

    /**
     * 二叉遍历，后序遍历；先记录左结点，再记录右结点，最后记录根结点
     * 通过入栈出栈方式解决：根结点先入栈，右结点再入栈，左结点最后入栈
     * 1. 先遍历根结点左子树，再遍历根结点右子树，最后是根结点
     * 2. 按照先入根结点，再入右结点，最后入左结点的顺序将结点入栈，直到结点没有左右子树
     * 3. 当结点没有左右子树时，说明已经到叶子结点
     * 3.1 记录结点值
     * 3.2 出栈第一个结点
     * 3.3 记录当前结点与前一个结点
     * 4.如果前一个结点是当前结点的左或右子树，记录当前结点值且出栈该结点
     */
    public static void afterOrder2(Node node) {
        Node cur, pre = null;
        List<Integer> data = new ArrayList<>();
        LinkedList<Node> stack = new LinkedList<>();
        stack.addFirst(node);
        while (!stack.isEmpty()) {
            cur = stack.peek();
            if (check(cur, pre)) {
                data.add(cur.value);
                stack.removeFirst();
                pre = cur;
            } else {
                if (cur.right != null) {
                    stack.addFirst(cur.right);
                }
                if (cur.left != null) {
                    stack.addFirst(cur.left);
                }
            }
        }
        System.out.println(data.toString());
    }

    /**
     * 递归
     */
    public static void afterOrderWithRecursion(Node node) {
        if (node == null) {
            return;
        }
        afterOrderWithRecursion(node.left);
        afterOrderWithRecursion(node.right);
        System.out.print(node.value + ",");
    }

    private static boolean check(Node cur, Node pre) {
        if (cur.left == null && cur.right == null) {
            return true;
        }
        return pre != null && (cur.left == pre || cur.right == pre);
    }
}
