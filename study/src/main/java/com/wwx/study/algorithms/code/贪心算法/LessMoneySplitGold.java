package com.wwx.study.algorithms.code.贪心算法;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 最小分割金条方案
 * <p>
 * 将长度为数组值的金条分割，每次分割都为当前金条长度，选择最优方案
 * 如：将金条分割为[2,3,5,9,3,4]段，金条长度总计26，最优方案：
 * </p>
 * <p>
 * 思路：哈夫曼编码，最优二叉树
 * </p>
 *
 * @author wuwenxi 2022-07-19
 */
public class LessMoneySplitGold {

    private static Integer lessMoney(Integer[] arr) {
        PriorityQueue<Integer> pQ = new PriorityQueue<>(Arrays.asList(arr));
        Integer sum = 0;
        Integer cur;
        while (pQ.size() > 1) {
            cur = pQ.poll() + pQ.poll();
            sum += cur;
            pQ.add(cur);
        }
        return sum;
    }

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{2, 3, 4, 7, 9, 2};
        int lessMoney = lessMoney(arr);
        System.out.println(lessMoney);

        // 27,16,11,7,4
    }
}
