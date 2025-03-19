package org.g2.starter.core.util.tree;

import java.util.Comparator;

/**
 * 红黑树满足条件：
 * 1. 每个结点要么是黑色，要么是红色
 * 2. 根节点是黑色
 * 3. 每个叶子结点点（NIL）是黑色
 * 4. 每个红色结点的两个子结点一定都是黑色
 * 5. 任意一结点到每个叶子结点的路径都包含数量相同的黑结点
 *
 * @author wenxi.wu@hand-chian.com 2021-03-11
 */
public class RedBlackTree<K, V>
        extends AbstractTree<K, V>
        implements Tree<K, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private TreeNode<K, V> root;
    private Comparator<? super K> comparator;

    public RedBlackTree() {
        this.comparator = null;
    }

    public RedBlackTree(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public V put(K key, V value) {
        ++size;
        return null;
    }

    @Override
    public TreeNode<K, V> getNode(K key) {
        return null;
    }

    /**
     * 插入新的结点需要重新平衡红黑树
     * 新插入的结点显然不会违背1.2.5个特性，
     * 第5个特性可能违背
     */
    void fixAfterInsertion(TreeNode<K, V> node) {
        TreeNode<K, V> p = node;
        // 当前结点不为空且父亲结点为红色
        while (p != null && isRed(parentOf(p))) {
            // 如果父亲结点是祖父结点的左结点
            if (parentOf(p) == leftOf(parentOf(parentOf(p)))) {
                // 如果叔叔结点为红色，交换颜色
                if (isRed(rightOf(parentOf(parentOf(p))))) {
                    // 改变颜色，祖父结点颜色为红色，左右结点为黑色
                    flipColor(parentOf(parentOf(p)));
                    // 从祖父结点继续向上找
                    p = parentOf(parentOf(p));
                }
                // 如果叔叔结点为黑色，跳转当前父结点
                else {
                    // 如果当前为右结点，左旋
                    if (p == rightOf(parentOf(p))) {
                        // 父节结点
                        p = parentOf(p);
                        // 将父结点进行左旋，
                        // 注意：转换后p指向父结点的左结点，原父结点转换为左结点，原右结点为父结点，父结点和左结点均为红色
                        rotateLeft(p);
                    }
                    // 如果当前指向为左结点，进行右旋
                    // 父亲结点设为黑色
                    setColor(parentOf(p), BLACK);
                    // 祖父结点设为红色
                    setColor(parentOf(parentOf(p)), RED);
                    // 将祖父结点右旋
                    // 注意：转换后p指向父结点的右结点，原父亲结点不变，原祖先结点变为兄弟结点，当前结点为红色，父亲结点为黑色，兄弟结点为红色
                    rotateRight(parentOf(parentOf(p)));
                }
            }
            // 父亲结点是祖父结点的右结点或父结点为根结点
            else {
                // 如果叔叔结点为红色，交换颜色
                if (isRed(leftOf(parentOf(parentOf(p))))) {
                    // 改变颜色，祖父结点颜色为红色，左右结点为黑色
                    flipColor(parentOf(parentOf(p)));
                    // 从祖父结点继续向上找
                    p = parentOf(parentOf(p));
                } else {
                    // 如果当前结点为左结点，右旋
                    if (p == leftOf(parentOf(p))) {
                        // 父结点
                        p = parentOf(p);
                        // 将父结点右旋
                        // 注意：转换后p指向父结点的右结点，原父结点转换为右结点，原左结点为父结点，父结点和右结点均为红色
                        rotateRight(p);
                    }
                    // 修改父结点为黑色
                    setColor(parentOf(p), BLACK);
                    // 修改祖父结点为红色
                    setColor(parentOf(parentOf(p)), RED);
                    // 将祖父结点左旋
                    // 注意：转换后p指向父结点的右结点，原父亲结点不变，原祖先结点变为兄弟结点，当前结点为红色，父亲结点为黑色，兄弟结点为红色
                    rotateLeft(parentOf(parentOf(p)));
                }
            }
        }
        root.color = BLACK;
    }

    /**
     * 红黑树删除后，进行平衡操作
     */
    void fixAfterDeletion(TreeNode<K, V> node) {

    }

    private TreeNode<K, V> createNode(K key, V value, TreeNode<K, V> parent) {
        return new TreeNode<>(key, value, RED, (TreeNode<K, V>) parent);
    }

    private boolean isRed(TreeNode<K, V> p) {
        return p != null && p.color == RED;
    }

    private void setColor(TreeNode<K, V> p, boolean color) {
        if (p != null) {
            p.color = color;
        }
    }

    private TreeNode<K, V> parentOf(TreeNode<K, V> node) {
        return node == null ? null : (TreeNode<K, V>) node.parent;
    }

    private TreeNode<K, V> leftOf(TreeNode<K, V> node) {
        return node == null ? null : (TreeNode<K, V>) node.left;
    }

    private TreeNode<K, V> rightOf(TreeNode<K, V> node) {
        return node == null ? null : (TreeNode<K, V>) node.right;
    }

    /**
     * 转换一个结点的两个红色子节点的颜色，同时将父节点颜色由黑变红
     */
    private void flipColor(TreeNode<K, V> h) {
        if (h != null) {
            h.color = RED;
            ((TreeNode<K, V>) h.left).color = BLACK;
            ((TreeNode<K, V>) h.right).color = BLACK;
        }
    }

    /**
     * 左旋：以某个结点作为支点(旋转结点)，其右子结点变为旋转结点的父结点，
     * 右子结点的左子结点变为旋转结点的右子结点，左子结点保持不变
     * 1. 旋转结点(h)右结点(p)作为新的根结点
     * 2. 右结点p的左子树作为旋转结点h的最新的右结点
     * 3. 旋转结点h作为右结点p的左结点
     */
    private void rotateLeft(TreeNode<K, V> h) {
        if (h != null) {
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
            } else if (h.parent.left == h) {
                h.parent.left = p;
            } else {
                h.parent.right = p;
            }
            p.left = h;
            h.parent = p;
        }
    }

    /**
     * 右旋：以某个结点作为支点(旋转结点)，其左子结点变为旋转结点的父结点，
     * 左子结点的右子结点变为旋转结点的左子结点，右子结点保持不变
     * 1. 旋转结点(h)左结点(p)作为新的根结点
     * 2. 左结点p的右子树作为旋转结点h的最新的左结点
     * 3. 旋转结点h作为左结点p的右结点
     */
    private void rotateRight(TreeNode<K, V> h) {
        if (h != null) {
            // 交换结点
            TreeNode<K, V> p = (TreeNode<K, V>) h.left;
            h.left = p.right;

            if (p.right != null) {
                p.right.parent = h;
            }
            // 交换父结点
            if (h.parent == null) {
                this.root = p;
            } else if (h.parent.left == h) {
                h.parent.left = p;
            } else {
                h.parent.right = h;
            }
            p.right = h;
            h.parent = p;
        }
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