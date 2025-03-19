package com.wwx.study.lfu;

import java.util.Map;

/**
 * @author wuwenxi 2023-12-19
 */
public class LFUCache<K, V> {
    /**
     * 存储数据
     */
    private Map<K, FreqNode<V>> cache;

    /**
     * 记录使用频率
     */
    private FreqList<V> head;

    public V put(K key, V v) {
        FreqNode<V> freqNode;
        if ((freqNode = cache.get(key)) != null) {
            addFreq(freqNode);
        } else {
            freqNode = newNode(v);
            cache.put(key, freqNode);
        }
        return v;
    }

    public V get(K key) {
        FreqNode<V> freqNode;
        if ((freqNode = cache.get(key)) != null) {
            addFreq(freqNode);
        }
        return freqNode != null ? freqNode.value : null;
    }

    private void addFreq(FreqNode<V> freqNode) {
        FreqList<V> freqList = freqNode.freqList;
        // 当前node从list中移除
        FreqNode<V> next = freqList.freqNode;
        while (true) {
            if (next == null) {
                break;
            }
            if (next.prv != null && next.prv == freqNode) {
                break;
            }
            next = next.prv;
        }
        if (next != null) {
            // 当前节点
            FreqNode<V> cur = next.prv;
            next.prv = cur.prv;
        }
        // 当前node移动到新到list中
        int freq = ++freqList.freq;
        FreqList<V> cur = head;
        while (cur.freq != freq) {
            if (cur.freq < freq) {
                cur = cur.prv;
                break;
            }
            cur = cur.next;
        }
    }

    private FreqNode<V> newNode(V value) {
        FreqNode<V> freqNode = new FreqNode<>(value);
        freqNode.freqList = newOrFindFreqList(freqNode);
        return freqNode;
    }

    private FreqList<V> newOrFindFreqList(FreqNode<V> freqNode) {
        FreqList<V> newNode;
        if (this.head.freq == 1) {
            newNode = head;
            freqNode.prv = head.freqNode;
            head.freqNode = freqNode;
        } else {
            newNode = new FreqList<>(1, freqNode);
            newNode.next = this.head;
            this.head = newNode;
            newNode.freqNode = freqNode;
        }
        return newNode;
    }

    private static class FreqNode<V> {
        private V value;
        private FreqList<V> freqList;
        private FreqNode<V> prv;

        FreqNode(V value) {
            this.value = value;
        }
    }

    private static class FreqList<V> {
        private int freq;
        private FreqNode<V> freqNode;
        private FreqList<V> prv;
        private FreqList<V> next;

        FreqList(int freq, FreqNode<V> freqNode) {
            this.freq = freq;
            this.freqNode = freqNode;
        }
    }
}
