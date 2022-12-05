package com.wwx.study.algorithms.code.排序;

/**
 * 插入排序
 *
 * <p>
 * 算法思想：
 * 1. 依次遍历每一个元素
 * 2. 当前元素与前一个元素做比较，如果小于则交换值，继续向前比较
 * </p>
 *
 * <p>
 * 时间复杂度：O(n^2)
 * </p>
 *
 * @author wuwenxi 2022-03-24
 */
public class Code_InsertionSort {

    public static void main(String[] args) {
//        Code_InsertionSort sort = new Code_InsertionSort();
//        int[] nums = {2, 1, 0, 1, 2, 3, 4, 3};
//        sort.insertionSort(nums);
//        System.out.println(Arrays.toString(nums));
    }

    public void insertionSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            // 只有当前循环当值比前一个值小，才进行循环
            for (int j = i; j > 0 && nums[j] < nums[j - 1]; j--) {
                int temp = nums[j - 1];
                nums[j - 1] = nums[j];
                nums[j] = temp;
            }
        }
    }
}
