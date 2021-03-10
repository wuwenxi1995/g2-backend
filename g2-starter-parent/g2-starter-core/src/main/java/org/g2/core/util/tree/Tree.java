package org.g2.core.util.tree;

import java.util.Iterator;

/**
 * @author wenxi.wu@hand-chian.com 2021-03-03
 */
public interface Tree<K, V> {

    /**
     * 添加节点
     *
     * @param key   键
     * @param value 值
     * @return 值
     */
    V put(K key, V value);

    /**
     * 查询值
     *
     * @param key 键
     * @return 值
     */
    V get(K key);

    /**
     * 是否包含键
     *
     * @param key 键
     * @return {@code true}
     */
    boolean containsKey(K key);

    /**
     * 树节点个数
     *
     * @return 树节点个数
     */
    int size();

    /**
     * 树是否为空
     *
     * @return true/false
     */
    boolean isEmpty();

    /**
     * 查找节点
     *
     * @param key 键
     * @return 节点
     */
    Tree.Entry<K, V> getNode(K key);

    /**
     * 取最小节点
     *
     * @return 节点
     */
    Tree.Entry<K, V> min();

    /**
     * 取最大节点
     *
     * @return 节点
     */
    Tree.Entry<K, V> max();

    /**
     * 向上取整
     * <p>
     * 如果给定的键key小于二叉查找树的根结点的键，那么小于等于key的最大键一定在根结点的左子树中;
     * </p>
     * <p>
     * 如果给定的键key大于二叉查找树的根结点，那么只有当根结点右子树中存在小于等于key的结点时，
     * 小于等于key的最大键才会出现在右子树中，否则根结点就是小于等于key的最大键。
     * </p>
     * 将“左”变为“右”(同时将小于变为大于)就能够得到{@link Tree#ceiling(java.lang.Object)}的算法。
     *
     * @param key 查找节点
     * @return 节点
     */
    Tree.Entry<K, V> floor(K key);

    /**
     * 向下取整
     * <p>
     * 如果给定的键key大于二叉查找树的根结点的键，那么大于等于key的最大键一定在根结点的右子树中;
     * </p>
     * <p>
     * 如果给定的键key小于二叉查找树的根结点，那么只有当根结点左子树中存在大于等于key的结点时，
     * 大于等于key的最大键才会出现在左子树中，否则根结点就是大于等于key的最大键。
     * </p>
     *
     * @param key 查找节点
     * @return 节点
     */
    Tree.Entry<K, V> ceiling(K key);

    /**
     * 假设我们想找到排名为k的键(即树中正好有k个小于它的键)。
     * <p>
     * 如果左子树中的结点数t大于k, 那么我们就继续(递归地)在左子树中查找排名为k的键;
     * 如果t等于k,我们就返回根结点中的键;
     * 如果t小于k，我们就(递归地)在右子树中查找排名为(k-t-1)的键
     * </p>
     *
     * @param num 排名
     * @return 节点
     */
    Tree.Entry<K, V> select(int num);

    /**
     * 假设我们想找到key键的排名
     * <p>
     * 如果给定的键和根结点的键相等，我们返回左子树中的结点总数t;
     * 如果给定的键小于根结点，我们会返回该键在左子树中的排名;
     * 如果给定的键大于根结点，我们会返回t + 1 (根结点)加上它在右子树中的排名。
     * </p>
     *
     * @param key 键
     * @return 排名
     */
    int rank(K key);

    /**
     * 删除指定键
     *
     * @param key 键
     * @return true/false
     */
    Tree.Entry<K, V> delete(K key);

    /**
     * 删除最小键
     *
     * @return true/false
     */
    Tree.Entry<K, V> deleteMin();

    /**
     * 删除最大键
     *
     * @return true/false
     */
    Tree.Entry<K, V> deleteMax();

    /**
     * 返回符合范围内的键
     *
     * @param key1 键1
     * @param key2 键2
     * @return 数组
     */
    Iterable<K> keys(K key1, K key2);

    interface Entry<K, V> {

        K getKey();

        V getValue();

        V setValue(V value);

        int deep();

        int size();

        Entry<K, V> min();

        Entry<K, V> max();

        Iterable<K> traversal();
    }
}
