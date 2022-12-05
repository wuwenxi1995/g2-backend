package com.wwx.study.algorithms.code.排序.heap;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 给定一个数据流，随时可以取得中位数
 *
 * <p>
 * 1.一个大根堆一个小根堆
 * 2.加入元素时比大根堆大大的值加入到小根堆
 * 3.大根堆和小根堆size不超过1，超过1将第一个元素加入到对方堆中
 * </p>
 *
 * @author wuwenxi 2022-07-20
 */
public class QuickFindMid {

    private int quickFindMid(int[] arr) {
        if (arr == null) {
            return -1;
        }
        if (arr.length < 2) {
            return arr[0];
        }
        // 小根堆
        PriorityQueue<Integer> minPq = new PriorityQueue<>();
        // 大根堆
        PriorityQueue<Integer> maxPq = new PriorityQueue<>(Comparator.comparing(Integer::intValue, Comparator.reverseOrder()));
        maxPq.offer(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxPq.peek()) {
                minPq.offer(arr[i]);
            } else {
                maxPq.offer(arr[i]);
            }
            if (Math.abs(maxPq.size() - minPq.size()) > 1) {
                if (maxPq.size() > minPq.size()) {
                    Integer max = maxPq.poll();
                    minPq.offer(max);
                } else {
                    Integer max = minPq.poll();
                    maxPq.offer(max);
                }
            }
        }
        return maxPq.poll();
    }
}
