package com.wwx.study.algorithms.code.排序;

/**
 * 冒泡排序
 *
 * <p>
 * 算法基本思想：循环遍历数组，将最大的值往后移动
 * 第一次遍历找到最大的值，移动到最后一个位置，
 * 第二次遍历找到剩余值最大的值，移动到倒数第二个位置
 * 依次循环，直到成为有序的数组
 * </p>
 *
 * <p>
 * 时间复杂度：O(n^2)
 * </p>
 *
 * @author wuwenxi 2022-03-24
 */
public class Code_BubbleSort {

    public void bubbleSort0(int[] nums) {
        if (nums == null || nums.length < 2) {
            return;
        }
        for (int i = nums.length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                // 前一个比后一个值大，进行交换
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
    }

}
