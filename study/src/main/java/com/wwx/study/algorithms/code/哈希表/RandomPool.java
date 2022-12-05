package com.wwx.study.algorithms.code.哈希表;

import java.util.HashMap;
import java.util.Map;

/**
 * 设计一种结构，将某个元素加入到结构中做到不重复加入，从结构中移除某个元素，
 * 等概率返回任何一个元素，要求这三个操作时间复杂度都在O(1)
 *
 * @author wuwenxi 2022-07-26
 */
public class RandomPool {

    private static class Pool<K> {
        private Map<K, Integer> keyIndexMap;
        private Map<Integer, K> indexKeyMap;
        private int size;

        public Pool() {
            this.keyIndexMap = new HashMap<>(16);
            this.indexKeyMap = new HashMap<>(16);
            this.size = 0;
        }

        public void insert(K key) {
            if (keyIndexMap.containsKey(key)) {
                return;
            }
            keyIndexMap.put(key, size);
            indexKeyMap.put(size++, key);
        }

        public void remove(K key) {
            if (!keyIndexMap.containsKey(key)) {
                return;
            }
            Integer removeIndex = keyIndexMap.get(key);
            int lastIndex = --size;
            K lastKey = indexKeyMap.get(lastIndex);
            keyIndexMap.put(lastKey, removeIndex);
            indexKeyMap.put(removeIndex, lastKey);
            keyIndexMap.remove(key);
            indexKeyMap.remove(lastIndex);
        }

        public K random() {
            if (size == 0) {
                return null;
            }
            return indexKeyMap.get((int) (Math.random() * size));
        }
    }
}
