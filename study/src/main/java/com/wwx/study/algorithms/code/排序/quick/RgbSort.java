package com.wwx.study.algorithms.code.排序.quick;

import java.util.Arrays;

/**
 *  颜色排序
 *
 * @author wuwenxi 2022-03-14
 */
public class RgbSort {
    private int a;

    public static void main(String[] args) {
        int[] a = {0, 0, 0, 2, 2, 2, 1, 1, 2, 1, 0, 0, 0, 1, 2, 2, 2, 1, 1, 1, 0};
        rgb(a);
        System.out.println(Arrays.toString(a));
    }

    private static void rgb(int[] nums) {
        int p0 = 0, p1 = nums.length - 1;
        for (int i = 0; i <= p1; i++) {
            // 逆向遍历
            // 确保交换后的nums[i]不是2
            while (i <= p1 && nums[i] == 2) {
                int temp = nums[i];
                nums[i] = nums[p1];
                nums[p1] = temp;
                --p1;
            }
            // 正向
            if (nums[i] == 0) {
                int temp = nums[i];
                nums[i] = nums[p0];
                nums[p0] = temp;
                p0++;
            }
        }
    }

    private static void swap(int[] a, int x, int y) {
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
    }
}
