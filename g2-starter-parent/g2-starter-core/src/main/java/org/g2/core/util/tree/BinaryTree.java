package org.g2.core.util.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树
 *
 * @author wenxi.wu@hand-chian.com 2021-03-03
 */
public class BinaryTree<K, V> extends AbstractTree<K, V> implements Tree<K, V> {

    private Node<K, V> root;

    private Comparator<? super K> comparator;

    public BinaryTree() {
        this.comparator = null;
    }

    public BinaryTree(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> node = (Node<K, V>) entry, newNode;
        if (node == null) {
            entry = newNode = createNode(key, value, null);
        } else {
            int compare;
            Node<K, V> parent;
            do {
                parent = node;
                compare = compare(node, key);
                if (compare < 0) {
                    node = node.left;
                } else if (compare > 0) {
                    node = node.right;
                } else {
                    return node.setValue(value);
                }
            } while (node != null);
            newNode = createNode(key, value, parent);
            if (compare > 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
        // 重新平衡二叉树
        fixAfterInsertion(newNode);
        size++;
        return value;
    }

    @Override
    public Node<K, V> getNode(K key) {
        if (entry == null) {
            return null;
        }
        Node<K, V> node = (Node<K, V>) entry;
        do {
            int compare = compare(node, key);
            if (compare < 0) {
                node = node.left;
            } else if (compare > 0) {
                node = node.right;
            } else {
                return node;
            }
        } while (node != null);
        return null;
    }

    @Override
    public Node<K, V> floor(K key) {
        return floor(root, key);
    }

    @Override
    public Node<K, V> ceiling(K key) {
        return ceiling(root, key);
    }

    @Override
    public Node<K, V> delete(K key) {
        // 找到要删除的节点
        Node<K, V> p = getNode(key);
        if (p == null) {
            return null;
        }
        // 找到替换节点
        Node<K, V> replacement = findReplacementNode(p);
        // 如果存在这样的替换节点
        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) {
                root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }

            p.left = p.right = p.parent = null;

            // 重新平衡二叉树
            fixAfterDeletion(replacement);
        }
        // 如果不存在替换节点，并且删除节点的父节点为空，则删除树
        else if (p.parent == null) {
            root = null;
        }
        // 如果不存在替换节点，并且删除节点的父节点不为空，将父节点左或右子树替换为空
        else {
            if (p.parent.left == p) {
                p.parent.left = null;
            } else if (p == p.parent.right) {
                p.parent.right = null;
            }
            p.parent = null;
        }
        return p;
    }

    @Override
    public Node<K, V> deleteMin() {
        Node<K, V> min = root.min();
        Node<K, V> parent = min.parent;
        if (min.right != null) {
            Node<K, V> newLeft = min.right;
            newLeft.parent = parent;
            parent.left = newLeft;
        } else {
            parent.left = null;
        }
        min.left = min.right = min.parent = null;
        return min;
    }

    @Override
    public Node<K, V> deleteMax() {
        Node<K, V> max = root.max();
        Node<K, V> parent = max.parent;
        if (max.left == null) {
            parent.right = null;
        } else {
            Node<K, V> newRight = max.left;
            newRight.parent = parent;
            parent.right = newRight;
        }
        max.left = max.right = max.parent = null;
        return max;
    }

    @Override
    public Iterable<K> keys(K key1, K key2) {
        if (root == null) {
            return null;
        }
        List<K> traverse = traverse(Traversal.IN_ORDER);
        int index1 = traverse.indexOf(key1);
        int index2 = traverse.indexOf(key2);
        if (index1 != -1 && index2 != -1) {
            return traverse.subList(index1, index2);
        }
        List<K> data = new ArrayList<>();
        if (index1 != -1) {
            data.add(key1);
        } else {
            data.add(key2);
        }
        return data;
    }

    @Override
    public List<K> traverse(Traversal traversal) {
        switch (traversal) {
            case PRE_ORDER:
                return preOrderTraversal(root);
            case IN_ORDER:
                return inOrderTraversal(root);
            case POST_ORDER:
                return postOrderTraversal(root);
            case DEFAULT:
            default:
                return traversal(root);
        }
    }

    private Node<K, V> floor(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int compare = compare(node, key);
        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return floor(node.left, key);
        }
        Node<K, V> t = floor(node.right, key);
        if (t != null) {
            return t;
        }
        return node;
    }

    private Node<K, V> ceiling(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int compare = compare(node, key);
        if (compare == 0) {
            return node;
        } else if (compare > 0) {
            return ceiling(node.right, key);
        }
        Node<K, V> t = ceiling(node.left, key);
        if (t != null) {
            return t;
        }
        return node;
    }

    private int compare(Node<K, V> node, K key) {
        int compare;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            compare = comparator.compare(key, node.key);
        } else {
            if (key == null) {
                throw new NullPointerException();
            }
            @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
            compare = k.compareTo(node.key);
        }
        return compare;
    }

    void fixAfterInsertion(Node<K, V> node) {
    }

    void fixAfterDeletion(Node<K, V> node) {
    }

    Node<K, V> findReplacementNode(Node<K, V> p) {
        // 如果删除节点存在左右子树，取右子树最小键
        Node<K, V> min = null;
        if (p.left != null && p.right != null) {
            // 找到替换节点
            min = p.right.min();
            Node<K, V> parent = min.parent;
            parent.left = null;
            if (min.right != null) {
                parent.left = min.right;
                min.right.parent = parent;
            }
            // 新的节点
            min.parent = null;
            p.left.parent = min;
            p.right.parent = min;
            min.left = p.left;
            min.right = p.right;
        }

        // 找到替换节点
        // 1.如果删除节点左右子树不同时存在，直接使用左或右子树作为替换节点
        // 2.如果删除节点左右子树同时存在，用在找到右子树最小键中找替换节点
        return min == null ? (p.left != null ? p.left : p.right) : min;
    }

    private Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        return new Node<>(key, value, parent);
    }

    static class Node<K, V> implements Tree.Entry<K, V> {
        K key;
        V value;
        Node<K, V> left, right, parent;

        Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public int deep() {
            // 深度优先遍历（DFS递归实现) : 不全部保留结点，占用空间少；有回溯操作，运行速度慢
            return dfs(this);
        }

        int dfs(Node<K, V> node) {
            if (node == null) {
                return 0;
            }
            return 1 + Math.max(dfs(node.left), dfs(node.right));
        }

        int bfs(Node<K, V> node) {
            Node<K, V> temp;
            // 使用队列 不是栈
            LinkedList<Node<K, V>> queue = new LinkedList<>();
            queue.offer(node);
            int deep = 0;
            // 当队列为空 结束遍历
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int index = 0; index < size; index++) {
                    temp = queue.poll();
                    if (temp == null) {
                        break;
                    }
                    if (temp.right != null) {
                        queue.offer(temp.right);
                    }
                    if (temp.left != null) {
                        queue.offer(temp.left);
                    }
                }
                deep++;
            }
            return deep;
        }

        @Override
        public Node<K, V> min() {
            Node<K, V> p = this;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        @Override
        public Node<K, V> max() {
            for (Node<K, V> p = this, temp; ; ) {
                if ((temp = p.right) == null) {
                    return p;
                }
                p = temp;
            }
        }
    }

    //
    //      参考博客：https://www.cnblogs.com/zhi-leaf/p/10813048.html
    // =================================================================

    /**
     * 先序遍历，先根结点，再左节点，最后右
     */
    private List<K> preOrderTraversal(Node<K, V> node) {
        List<K> data = new ArrayList<>();
        BinaryTree.Node<K, V> p = node;
        if (p == null) {
            return null;
        }
        LinkedList<BinaryTree.Node<K, V>> stack = new LinkedList<>();
        while (p != null || !stack.isEmpty()) {
            while (p != null) {
                data.add(p.key);
                stack.addFirst(p);
                p = p.left;
            }
            if (!stack.isEmpty()) {
                p = stack.removeFirst();
                p = p.right;
            }
        }
        return data;
    }

    /**
     * 中序遍历，先左节点，再根节点，最后右节点
     */
    private List<K> inOrderTraversal(Node<K, V> node) {
        BinaryTree.Node<K, V> p;
        if ((p = node) == null) {
            return null;
        }
        List<K> data = new ArrayList<>();
        LinkedList<BinaryTree.Node<K, V>> stack = new LinkedList<>();
        while (p != null || !stack.isEmpty()) {
            while (p != null) {
                stack.addFirst(p);
                p = p.left;
            }
            if (!stack.isEmpty()) {
                p = stack.removeFirst();
                data.add(p.key);
                p = p.right;
            }
        }
        return data;
    }

    /**
     * 后序遍历，先左节点，再右节点，最后根节点
     */
    private List<K> postOrderTraversal(Node<K, V> node) {
        // 临时节点，记录当前节点和前一个节点
        BinaryTree.Node<K, V> cur, pre = null;
        if (node == null) {
            return null;
        }
        List<K> data = new ArrayList<>();
        LinkedList<BinaryTree.Node<K, V>> stack = new LinkedList<>();
        stack.addFirst(node);
        while (!stack.isEmpty()) {
            // 不取出节点
            cur = stack.peek();
            // 如果当前节点为根节点或当前节点是前一个节点的父节点
            if ((cur.left == null && cur.right == null) || (pre != null && (cur.left == pre || cur.right == pre))) {
                data.add(node.key);
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
        return data;
    }

    /**
     * 层次遍历，广度优先
     */
    private List<K> traversal(Node<K, V> node) {
        BinaryTree.Node<K, V> p;
        if ((p = node) == null) {
            return null;
        }
        Queue<BinaryTree.Node<K, V>> queue = new LinkedList<>();
        queue.offer(p);
        List<K> data = new ArrayList<>();
        while (!queue.isEmpty()) {
            p = queue.poll();
            data.add(p.key);
            if (p.left != null) {
                queue.offer(p.left);
            }
            if (p.right != null) {
                queue.offer(p.right);
            }
        }
        return data;
    }
}
