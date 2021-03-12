package org.g2.core.util.tree;

import java.util.Comparator;

/**
 * 红黑树满足条件：
 * 1. 每个节点或者是黑色，或者是红色。根结点为黑色
 * 2. 没有任何一个结点同时和两个红结点相连
 * 3. 如果一个节点是红色的，则它的子节点必须是黑色的
 * 4. 该树为完美黑色平衡，即任意空链接到根结点经过的黑色结点数量相同
 * 5. 空链接为为黑色节点
 *
 * @author wenxi.wu@hand-chian.com 2021-03-11
 */
public class RedBlackTree<K, V> extends BinaryTree<K, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    public RedBlackTree(Comparator<? super K> comparator) {
        super(comparator);
    }

    @Override
    public V put(K key, V value) {
        TreeNode<K, V> p, newNode;
        if ((p = (TreeNode<K, V>) root) == null) {
            root = newNode = createNode(key, value, null);
        } else {
            int compare;
            TreeNode<K, V> parent;
            do {
                parent = p;
                compare = compare(p, key);
                if (compare == 0) {
                    return p.setValue(value);
                } else if (compare < 0) {
                    p = (TreeNode<K, V>) p.left;
                } else {
                    p = (TreeNode<K, V>) p.right;
                }
            } while (p != null);
            newNode = createNode(key, value, parent);
            if (compare < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
        balanceInsertion(newNode);
        ++size;
        return value;
    }

    private void balanceInsertion(Node<K, V> node) {
        // 由当前结点向上找
        while (node.parent != null) {
            node = node.parent;

            // 如果左子结点为黑色且右子结点为红色，左旋
            if (!isRed(node.left) && isRed(node.right)) {
                node = rotateLeft(node);
            }
            // 如果左子节点都为红色且左子节点的左子节点也是红色，右旋
            if (isRed(node.left) && isRed(node.left.left)) {
                node = rotateRight(node);
            }
            // 如果左右子节点均为红色，进行颜色变换
            if (isRed(node.left) && isRed(node.right)) {
                flipColor(node);
            }
        }
        ((TreeNode<K, V>) root).color = BLACK;
    }

    private boolean isRed(Node<K, V> treeNode) {
        TreeNode<K, V> p = (TreeNode<K, V>) treeNode;
        return p != null && p.color == RED;
    }

    private TreeNode<K, V> parentOf(Node<K, V> node) {
        return node == null ? null : (TreeNode<K, V>) node.parent;
    }

    /**
     * 转换一个结点的两个红色子节点的颜色，同时将父节点颜色由黑变红
     */
    private void flipColor(Node<K, V> node) {
        TreeNode<K, V> h = (TreeNode<K, V>) node;
        h.color = RED;
        ((TreeNode<K, V>) h.left).color = BLACK;
        ((TreeNode<K, V>) h.right).color = BLACK;
    }

    /**
     * 左旋
     * 1. 旋转结点(h)右结点(p)作为新的根结点
     * 2. 右结点p的左子树作为旋转结点h的最新的右结点
     * 3. 旋转结点h作为右结点p的左结点
     */
    private TreeNode<K, V> rotateLeft(Node<K, V> node) {
        TreeNode<K, V> h = (TreeNode<K, V>) node;
        // 结点交换
        TreeNode<K, V> p = (TreeNode<K, V>) h.right;
        h.right = p.left;

        if (p.left != null) {
            p.left.parent = h;
        }
        // 交换父结点
        p.parent = h.parent;
        // 如果父节点为空，则新的结点为根结点
        if (h.parent == null) {
            this.root = p;
        } else {
            if (h.parent.left == node) {
                h.parent.left = p;
            } else {
                h.parent.right = p;
            }
        }

        p.left = h;
        h.parent = p;
        // 交换颜色
        p.color = h.color;
        // 旋转后的结点为红色
        h.color = RED;
        return p;
    }

    /**
     * 右旋
     * 1. 旋转结点(h)左结点(p)作为新的根结点
     * 2. 左结点p的右子树作为旋转结点h的最新的左结点
     * 3. 旋转结点h作为左结点p的右结点
     */
    private TreeNode<K, V> rotateRight(Node<K, V> node) {
        TreeNode<K, V> h = (TreeNode<K, V>) node;
        // 交换结点
        TreeNode<K, V> p = (TreeNode<K, V>) h.left;
        h.left = p.right;

        if (h.right != null) {
            h.right.parent = h;
        }
        // 交换父结点
        if (h.parent == null) {
            this.root = p;
        } else {
            if (h.parent.left == node) {
                h.parent.left = p;
            } else {
                h.parent.right = h;
            }
        }
        p.right = h;
        h.parent = p;
        // 交换颜色
        p.color = h.color;
        // 旋转后的颜色为红色
        h.color = RED;
        return p;
    }

    private TreeNode<K, V> createNode(K key, V value, TreeNode<K, V> parent) {
        return new TreeNode<>(key, value, RED, parent);
    }

    static class TreeNode<K, V> extends BinaryTree.Node<K, V> {

        private boolean color;

        TreeNode(K key, V value, boolean color, TreeNode<K, V> parent) {
            super(key, value, parent);
            this.color = color;
        }

        @Override
        public int deep() {
            return bfs(this);
        }
    }
}