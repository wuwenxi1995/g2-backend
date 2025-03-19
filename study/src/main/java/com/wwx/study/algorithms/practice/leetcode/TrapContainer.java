package com.wwx.study.algorithms.practice.leetcode;

/**
 * 接雨水问题
 * https://leetcode.cn/problems/trapping-rain-water/description/
 * 盛水容器问题
 * https://leetcode.cn/problems/container-with-most-water/description/
 *
 * @author wuwenxi 2023-01-16
 */
public class TrapContainer {

    /**
     * 接雨水
     * <p>
     * 双指针，指向左右边界
     * </p>
     */
    public int trap(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return 0;
        }
        int left = 0, right = arr.length - 1, res = 0;
        while (left < right && arr[left + 1] > arr[left]) {
            left++;
        }
        while (right > left && arr[right - 1] > arr[right]) {
            right--;
        }
        while (left < right) {
            int leftHeight = arr[left], rightHeight = arr[right];
            if (leftHeight <= rightHeight) {
                while (left < right && leftHeight > arr[++left]) {
                    res += leftHeight - arr[left];
                }
            } else {
                while (left < right && rightHeight > arr[--right]) {
                    res += rightHeight - arr[right];
                }
            }
        }
        return res;
    }

    /**
     * 容器盛水
     */
    public int maxArea(int[] arr) {
        return -1;
    }
}
