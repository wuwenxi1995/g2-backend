package com.wwx.study.algorithms.code.排序.quick;

import java.util.Arrays;

/**
 * 荷兰国旗问题：给定一个数组arr，和一个数num，将所有小于num的数放数组左边，大于num的数放数组右边，等于num的数放中间；要求时间复杂度为O(1)
 *
 * <p>
 * 基本思路：快速排序
 * 1. 定义两个指针p1,p2分别指向数组头尾
 * 2. 遍历数组
 * 2.1 如果当前数小于num，则当前数和p1指针指向的位置交换，p1++
 * 2.2 如果当前数等于num，不做任何操作
 * 2.3 如果当前数大于num，则当前数与p2指针指向的位置交换，p2--，i位置不动，继续校验i位置上的数与校验数的大小
 * </p>
 *
 * @author wuwenxi 2022-03-28
 */
public class NetherlandsFlag {

    public static void main(String[] args) {
        int[] arr = {0, 3, 5, 6, 4, 2, 7, 8, 0, 5, 5, 3, 9, 5};
        NetherlandsFlag init = new NetherlandsFlag();
        init.sort(arr, 5);
        System.out.println(Arrays.toString(arr));
    }

    public void sort(int[] arr, int checkNum) {
        int p1 = 0, p2 = arr.length - 1;
        for (int i = 0; i < arr.length && i <= p2; i++) {
            while (arr[i] > checkNum) {
                swap(arr, i, p2);
                p2--;
            }
            if (arr[i] < checkNum) {
                swap(arr, i, p1);
                p1++;
            }
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}
