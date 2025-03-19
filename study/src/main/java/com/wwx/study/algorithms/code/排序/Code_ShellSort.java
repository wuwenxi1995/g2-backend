package com.wwx.study.algorithms.code.排序;

/**
 * 希尔排序
 *
 * @author wuwenxi 2022-03-30
 */
public class Code_ShellSort {


    public void shellSort(int[] arr, int n) {
        if (arr == null || arr.length < 1) {
            return;
        }
        int gap = n >> 1;
        int i, j;
        while (gap > 0) {
            for (i = gap; i < n; i++) {
                j = i - gap;

            }
            gap >>= 1;
        }
    }
}
