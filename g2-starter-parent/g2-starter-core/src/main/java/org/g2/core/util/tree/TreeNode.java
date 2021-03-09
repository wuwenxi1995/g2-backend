package org.g2.core.util.tree;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * 二叉树
 *
 * @author wenxi.wu@hand-chian.com 2021-03-03
 */
public class TreeNode<K, V> implements Tree<K, V> {

    private Node<K, V> root;

    private Comparator<K> comparator;

    public TreeNode() {
        this.comparator = null;
    }

    public TreeNode(Comparator<K> comparator) {
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
        int tempCompare = 0;
        for (Node<K, V> p = root, temp = null; ; ) {
            if (p == null) {
                return null;
            }
            int compare = compare(p, key);
            if (compare == 0) {
                // 1. 如果当前节点为叶子节点，直接删除当前节点
                if (p.left == null && p.right == null) {
                    // 如果当前节点为根节点
                    if (temp == null) {
                        this.root = null;
                        return null;
                    }
                    // 删除左或右子树
                    if (tempCompare > 0) {
                        temp.left = null;
                    } else {
                        temp.right = null;
                    }
                    return null;
                }
                // 2. 找到新的节点
                return findNewNode(p, temp, tempCompare);
            }
            tempCompare = compare;
            temp = p;
            if (compare < 0) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
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
        return max;
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

    private Node<K, V> findNewNode(Node<K, V> p, Node<K, V> temp, int tempCompare) {
        return null;
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

    protected void fixAfterInsertion(Node<K, V> node) {
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
                    if (temp.left != null) {
                        queue.offer(temp.left);
                    }
                    if (temp.right != null) {
                        queue.offer(temp.right);
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
            for (Node<K, V> p = this, temp; ; ) {
                if ((temp = p.left) == null) {
                    return p;
                }
                p = temp;
            }
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

        void resize(boolean increment) {
            synchronized (TreeNode.class) {
                for (Node<K, V> p = this; ; ) {
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
    }
}
