package com.wwx.study.algorithms.code.排序;

import java.util.Arrays;

/**
 * 快速排序
 *
 * <p>
 * 基本思想：
 * 1. 随机选择数组中一个数作为排序值
 * 2. 定义两个指针指向数组第一个位置和最后一个位置
 * 3. 遍历数组
 * 3.1 当前数小于排序值，与左边指针位置的值做交换
 * 3.2 当前数大于排序值，与右边指针位置的值做交换，并且继续判断交换后的值与排序值的大小关系
 * 3.3 当前数等于排序值，不做处理
 * 4. 将左边指针之前的数组和右边指针之后的数组递归执行第3步
 * </p>
 *
 * <p>
 * 时间复杂度：最好情况为O(N)；最坏情况为O(N^2)；平均O(N*logN)
 * 空间复杂度：O(logN)
 * </p>
 *
 * @author wuwenxi 2022-03-28
 */
public class Code_QuickSort {

    public static void main(String[] args) {
        int[] arr = {0, 3, 5, 6, 4, 2, 7, 8, 0, 5, 5, 3, 9, 5};
        Code_QuickSort sort = new Code_QuickSort();
        sort.quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            swap(arr, low + (int) (Math.random() * (high - low + 1)), high);
            int[] partition = partition(arr, low, high);
            quickSort(arr, low, partition[0] - 1);
            quickSort(arr, partition[1] + 1, high);
        }
    }

    private int[] partition(int[] arr, int low, int high) {
        int checkNum = arr[high];
        int p1 = low, p2 = high;
        for (int i = low; i < high && i <= p2; i++) {
            while (arr[i] > checkNum) {
                swap(arr, i, p2);
                p2--;
            }
            if (arr[i] < checkNum) {
                swap(arr, i, p1);
                p1++;
            }
        }
        // 返回边界
        return new int[]{p1, p2};
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
