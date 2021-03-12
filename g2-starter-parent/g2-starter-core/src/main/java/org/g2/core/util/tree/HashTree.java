package org.g2.core.util.tree;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-04
 */
public class HashTree<K, V> extends BinaryTree<K, V> {

    private HashNode<K, V> root;
    private int size;

    static class HashNode<K, V> extends Node<K, V> {

        int hash;

        HashNode(int hash, K key, V value, HashNode<K, V> parent) {
            super(key, value, parent);
            this.hash = hash;
        }

        @Override
        public int deep() {
            // 广度优先遍历（BFS层次遍历）: 保留全部结点，占用空间大； 无回溯操作，运行速度快。
            return bfs(this);
        }
    }

    @Override
    public V put(K key, V value) {
        return putValue(hash(key), key, value);
    }

    private V putValue(int hash, K key, V value) {
        HashNode<K, V> p = root;
        if (p == null) {
            p = new HashNode<>(hash, key, value, null);
        } else {
            // 出现hash冲突
            if (p.hash == hash) {
                // key相同的情况，替换value
                if (p.key == key || p.key.equals(key)) {
                    V oldValue = p.value;
                    p.value = value;
                    return oldValue;
                }
                // 出现hash冲突，key不同
                else {
                    if (p.left == null) {
                        p.left = newNode(hash, key, value, p);
                    } else if (p.right == null) {
                        p.right = newNode(hash, key, value, p);
                    } else {
                        HashNode<K, V> temp = (HashNode<K, V>) p.left;
                        HashNode<K, V> newNode = newNode(hash, key, value, p);
                        p.left = newNode;
                        // temp.parent = newNode;
                        newNode.left = temp;
                    }
                    // 重新平衡二叉树
                }
            } else if (p.hash > hash) {
                putValue(hash, key, value, p, true);
            } else {
                putValue(hash, key, value, p, false);
            }
        }
        root = p;
        ++size;
        return value;
    }

    private void putValue(int hash, K key, V value, HashNode<K, V> node, boolean left) {
        if (left) {
            if (node.left == null) {
                node.left = newNode(hash, key, value, node);
            }
        } else {
            if (node.right == null) {
                node.right = newNode(hash, key, value, node);
            }
        }
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private HashNode<K, V> newNode(int hash, K k, V value, HashNode<K, V> parent) {
        return new HashNode<>(hash, k, value, parent);
    }

    @Override
    Node<K, V> findReplacementNode(Node<K, V> p) {
        return null;
    }

    /**
     * 2次扰动函数
     */
    private int hash(Object key) {
        int h;
        // hash值与自己右移16位进行异或运算（高低位异或）
        return key == null ? 0 : ((h = key.hashCode()) ^ (h >>> 16));
    }
}
