package com.wwx.study.algorithms.practice.leetcode;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * https://leetcode.cn/problems/ipo/
 *
 * <p>
 *
 * </p>
 *
 * @author wuwenxi 2022-07-19
 */
public class IPO {

    private static class Pair {
        /**
         * 利润
         */
        private int profits;
        /**
         * 成本
         */
        private int capital;

        Pair(int profits, int capital) {
            this.profits = profits;
            this.capital = capital;
        }

        public int getProfits() {
            return profits;
        }

        public int getCapital() {
            return capital;
        }
    }

    public static int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        // 成本小根堆
        PriorityQueue<Pair> minPq = new PriorityQueue<>(Comparator.comparing(Pair::getCapital));
        // 利润大根堆
        PriorityQueue<Pair> maxPq = new PriorityQueue<>(Comparator.comparing(Pair::getProfits, Comparator.reverseOrder()));
        // 所有项目成本加入小根堆
        for (int i = 0; i < profits.length; i++) {
            minPq.add(new Pair(profits[i], capital[i]));
        }
        for (int i = 0; i < k; i++) {
            // 选择成本比当前资金小于等于的项目，加入到大根堆
            while (!minPq.isEmpty() && minPq.peek().capital <= w) {
                maxPq.add(minPq.poll());
            }
            if (maxPq.isEmpty()) {
                break;
            }
            // 从大根堆中选择利润最高到项目，增加资金
            w += maxPq.poll().profits;
        }
        return w;
    }

    public static void main(String[] args) {
        int[] profits = new int[]{1, 2, 3};
        int[] capital = new int[]{0, 1, 2};
        int maximizedCapital = findMaximizedCapital(3, 0, profits, capital);
        System.out.println(maximizedCapital);
    }

}
