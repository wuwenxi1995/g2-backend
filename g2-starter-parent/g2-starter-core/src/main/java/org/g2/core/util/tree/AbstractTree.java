package org.g2.core.util.tree;

import java.util.List;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-15
 */
public abstract class AbstractTree<K, V> implements Tree<K, V> {

    Entry<K, V> entry;
    int size;

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        Entry<K, V> node = getNode(key);
        return node == null ? null : node.getValue();
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Entry<K, V> min() {
        return entry.min();
    }

    @Override
    public Entry<K, V> max() {
        return entry.max();
    }

    @Override
    public Entry<K, V> floor(K key) {
        return null;
    }

    @Override
    public Entry<K, V> ceiling(K key) {
        return null;
    }

    @Override
    public Entry<K, V> select(int num) {
        return null;
    }

    @Override
    public int rank(K key) {
        return 0;
    }

    @Override
    public Entry<K, V> delete(K key) {
        return null;
    }

    @Override
    public Entry<K, V> deleteMin() {
        return null;
    }

    @Override
    public Entry<K, V> deleteMax() {
        return null;
    }

    @Override
    public Iterable<K> keys(K key1, K key2) {
        return null;
    }

    @Override
    public List<K> traverse(Traversal traversal) {
        return null;
    }
}
