package com.wwx.study.algorithms.code.排序;

/**
 * 选择排序
 *
 * <p>
 * 算法思想：
 * 从数组第一个开始遍历，找到最小值，与第一个值交换
 * 再从第二个开始遍历，找到最小值，与第二个值交换
 * 依次进行
 * 最终得到排好序的数组
 * </p>
 *
 * <p>
 * 时间复杂度：O(n^2)
 * </P>
 *
 * @author wuwenxi 2022-03-24
 */
public class Code_SelectionSort {

    public static void main(String[] args) {
//        int[] nums = {0, 2, 3, 9, 6, 1, 5, 4};
//        selectionSort(nums);
//        System.out.println(Arrays.toString(nums));
    }

    public void selectionSort(int[] nums) {
        if (nums == null || nums.length < 1) {
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            int min = i;
            for (int j = i; j < nums.length; j++) {
                if (nums[j] < nums[min]) {
                    min = j;
                }
            }
            if (min != i) {
                int temp = nums[min];
                nums[min] = nums[i];
                nums[i] = temp;
            }
        }
    }
}
