package org.g2.core.util.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树
 *
 * @author wenxi.wu@hand-chian.com 2021-03-03
 */
public class BinaryTree<K, V> implements Tree<K, V> {

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
        Node<K, V> node = root;
        if (node == null) {
            root = createNode(key, value, null);
            return value;
        }
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
        Node<K, V> newNode = createNode(key, value, parent);
        if (compare > 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        // 重新平衡二叉树
        fixAfterInsertion(newNode);
        return value;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    @Override
    public int size() {
        return root == null ? 0 : root.num;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Node<K, V> getNode(K key) {
        if (root == null) {
            return null;
        }
        Node<K, V> node = root;
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
    public Node<K, V> min() {
        return root.min();
    }

    @Override
    public Node<K, V> max() {
        return root.max();
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
    public Node<K, V> select(int num) {
        for (Node<K, V> p = root; ; ) {
            if (p == null) {
                return null;
            }
            int left = p.left == null ? 0 : p.left.num;
            if (left > num) {
                p = p.left;
            } else if (left < num) {
                num = num - left - 1;
                p = p.right;
            } else {
                return p;
            }
        }
    }

    @Override
    public int rank(K key) {
        int num = 0;
        for (Node<K, V> p = root; ; ) {
            if (p == null) {
                return -1;
            }
            int left = p.left == null ? 0 : p.left.num;
            int compare = compare(p, key);
            if (compare == 0) {
                return left + num;
            } else if (compare < 0) {
                p = p.left;
            } else {
                num = num + left + 1;
                p = p.right;
            }
        }
    }

    @Override
    public Node<K, V> delete(K key) {
        // 找到要删除的节点
        Node<K, V> p = getNode(key);
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

        // 重新平衡二叉树
        fixAfterInsertion(root);
        // 重新计算各个节点数
        resize();
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
        parent.resize(false);
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
        parent.resize(false);
        max.left = max.right = max.parent = null;
        return max;
    }

    @Override
    public Iterable<K> keys(K key1, K key2) {
        if (root == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<K> traverse = (List<K>) Traversal.IN_ORDER.traverse(root);
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

    void resize() {
        root.resize();
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
        return new Node<>(key, value, 1, parent);
    }

    static class Node<K, V> implements Tree.Entry<K, V> {
        private int num;
        K key;
        V value;
        Node<K, V> left, right, parent;

        Node(K key, V value, int num, Node<K, V> parent) {
            this.num = num;
            this.key = key;
            this.value = value;
            this.parent = parent;
            resize(true);
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
        public int size() {
            return num;
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

        @SuppressWarnings("unchecked")
        @Override
        public Iterable<K> traversal() {
            return (List<K>) Traversal.IN_ORDER.traverse(this);
        }

        private void resize(boolean increment) {
            synchronized (this) {
                for (Node<K, V> p = parent; ; ) {
                    if (p == null) {
                        return;
                    }
                    if (increment) {
                        p.num++;
                    } else {
                        p.num--;
                    }
                    p = p.parent;
                }
            }
        }

        /**
         * 计算当前树节点
         */
        private void resize() {
            resizeDfs(this);
        }

        /**
         * 深度优先遍历（DFS递归实现)
         */
        int resizeDfs(Node<K, V> node) {
            if (node == null) {
                return 0;
            }
            node.num = 1 + resizeDfs(node.left) + resizeDfs(node.right);
            return node.num;
        }

        /**
         * 广度优先遍历（BFS递归实现)
         */
        void resizeBfs() {

        }
    }

    //
    //      参考博客：https://www.cnblogs.com/zhi-leaf/p/10813048.html
    // =================================================================
    @SuppressWarnings("unchecked")
    enum Traversal {
        // 先序遍历
        PRE_ORDER {
            @Override
            List traverse(BinaryTree.Node node) {
                List data = new ArrayList<>();
                BinaryTree.Node p = node;
                if (p == null) {
                    return null;
                }
                LinkedList<BinaryTree.Node> stack = new LinkedList<>();
                while (p != null || !stack.isEmpty()) {
                    if (p != null) {
                        data.add(p.key);
                        stack.addFirst(p);
                        p = p.left;
                    } else {
                        p = stack.removeFirst();
                        p = p.right;
                    }
                }
                return data;
            }
        },
        // 中序遍历
        IN_ORDER {
            @Override
            List traverse(BinaryTree.Node node) {
                BinaryTree.Node p;
                if ((p = node) == null) {
                    return null;
                }
                List data = new ArrayList<>();
                LinkedList<BinaryTree.Node> stack = new LinkedList<>();
                //
                while (p != null || !stack.isEmpty()) {
                    if (p != null) {
                        stack.addFirst(p);
                        p = p.left;
                    } else {
                        p = stack.removeFirst();
                        data.add(p.key);
                        p = p.right;
                    }
                }
                return data;
            }
        },
        // 后序遍历
        POST_ORDER {
            @Override
            List traverse(BinaryTree.Node node) {
                // 临时节点，记录当前节点和前一个节点
                BinaryTree.Node cur, pre = null;
                if (node == null) {
                    return null;
                }
                List data = new ArrayList<>();
                LinkedList<BinaryTree.Node> stack = new LinkedList<>();
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
        },
        // 默认使用层次遍历
        DEFAULT {
            @Override
            List traverse(BinaryTree.Node node) {
                BinaryTree.Node p;
                if ((p = node) == null) {
                    return null;
                }
                Queue<BinaryTree.Node> queue = new LinkedList<>();
                queue.offer(p);
                List data = new ArrayList<>();
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
        };

        abstract List traverse(BinaryTree.Node node);
    }
}
