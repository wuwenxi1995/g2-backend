package com.wwx.study.algorithms.code.图.practice;

import com.wwx.study.algorithms.code.图.Edge;
import com.wwx.study.algorithms.code.图.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Dijkstra算法优化
 *
 * @author wuwenxi 2022-07-22
 */
public class Dijkstra {

    private <T> Map<Node<T>, Integer> dijkstra(Node<T> head, int size) {
        Map<Node<T>, Integer> result = new HashMap<>(16);
        NodeHeap<T> heap = new NodeHeap<>(size);
        heap.addOrUpdateOrIgnoreElement(head, 0);
        while (!heap.isEmpty()) {
            Pair<Node<T>, Integer> pop = heap.pop();
            if (pop == null) {
                break;
            }
            Node<T> cur = pop.first;
            for (Edge<T> edge : cur.getEdges()) {
                heap.addOrUpdateOrIgnoreElement(edge.getTo(), pop.second + edge.getWeight());
            }
            result.put(cur, pop.second);
        }
        return result;
    }

    private static class Pair<K, V> {
        private K first;
        private V second;

        Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
    }

    private static class NodeHeap<T> {

        // 存储结构
        private Pair<Node<T>, Integer>[] elements;
        // 索引
        private Map<Node<T>, Integer> index;
        // 大小
        private int size;

        NodeHeap(int size) {
            this.elements = new Pair[size];
            this.index = new HashMap<>(size << 1 + 1);
        }

        boolean isEmpty() {
            return size == 0;
        }

        Pair<Node<T>, Integer> pop() {
            if (size == 0) {
                return null;
            }
            // 0 和 size - 1 位置元素交换, 并删除size - 1位置元素
            int s = --size;
            Pair<Node<T>, Integer> element = elements[0];
            Pair<Node<T>, Integer> x = elements[s];
            elements[s] = null;
            if (s != 0) {
                siftDown(0, x);
            }
            // 更新索引位置为-1
            index.computeIfPresent(element.first, (key, value) -> -1);
            return element;
        }

        void addOrUpdateOrIgnoreElement(Node<T> node, Integer weight) {
            // 更新
            if (this.index.containsKey(node) && index.get(node) != -1) {
                Pair<Node<T>, Integer> element = elements[index.get(node)];
                // ignore
                if (element.second <= weight) {
                    return;
                }
                element.second = weight;
                if (size == 0) {
                    return;
                }
                // 调整小根堆
                heapify();
            }
            // 新增
            else if (!this.index.containsKey(node)) {
                int i = this.size;
                if (i == this.elements.length) {
                    throw new ArrayIndexOutOfBoundsException("超过小根堆大小");
                }
                this.size = i + 1;
                Pair<Node<T>, Integer> newNode = new Pair<>(node, weight);
                if (i == 0) {
                    elements[0] = newNode;
                    index.put(node, 0);
                } else {
                    // 调整小根堆
                    siftUp(i, newNode);
                }
            }
        }

        private void siftUp(int k, Pair<Node<T>, Integer> newNode) {
            while (k > 0) {
                int parent = (k - 1) >>> 1;
                Pair<Node<T>, Integer> element = elements[parent];
                // 小根堆
                if (newNode.second >= element.second) {
                    break;
                }
                elements[k] = element;
                k = parent;
            }
            elements[k] = newNode;
            // 存放所在位置
            index.put(newNode.first, k);
        }

        private void heapify() {
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i, elements[i]);
            }
        }

        /**
         * 找到x元素在小根堆中的合适位置
         */
        private void siftDown(int k, Pair<Node<T>, Integer> x) {
            int half = size >>> 1;
            while (k < half) {
                // 左子树位置
                int child = (k << 1) + 1;
                Pair<Node<T>, Integer> c = elements[child];
                // 右子树
                int right = child + 1;
                // * 取左右子树值较大元素
                if (right < size &&
                        c.second > elements[right].second) {
                    // 下一节点选取右子树
                    c = elements[child = right];
                }
                // 如果节点x值小于等于左右子树值较大元素的值, 结束
                if (x.second <= c.second) {
                    break;
                }
                elements[k] = c;
                // 更新c元素索引位置
                update(c.first, k);
                // 继续
                k = child;
            }
            elements[k] = x;
            update(x.first, k);
        }

        private void update(Node<T> key, int index) {
            this.index.computeIfPresent(key, (k, v) -> index);
        }
    }
}
