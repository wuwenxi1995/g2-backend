package com.wwx.study.algorithms.code.排序;

import java.util.Arrays;

/**
 * 桶排序：
 * <p>
 * 计数排序：
 * 基数排序：
 * </p>
 *
 * <p>
 * 基本思想：
 * 1. 找到数组最大值，统计最大值有多少个十位，不够最大位的值相等于补0；如：023,118,093
 * 2. 维护一个长度为10的bucket数组
 * 2.1 遍历数组，从每个数个位数开始将值维护到对应bucket数组中下标位置
 * 2.2 bucket从左向右将值取出来依次赋值到对应arr数组中，bucket满足先进后出
 * 3. 进行按照2.1、2.2步骤执行十位、百位等操作，
 * </p>
 *
 * <p>
 * 时间复杂度：
 * </p>
 *
 * @author wuwenxi 2022-03-30
 */
public class Code_RadixSort {

    public static void main(String[] args) {
        int[] arr = {15, 38, 24, 19, 56, 129, 110, 99, 29, 39, 49, 59, 59, 149};
        radixSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void radixSort(int[] arr) {
        if (arr == null || arr.length < 1) {
            return;
        }
        radixSort(arr, 0, arr.length - 1, maxBits(arr));
    }

    public static void radixSort(int[] arr, int low, int high, int digit) {
        final int bucketSize = 10;
        int i = 0, j = 0;
        // 辅助数组
        int[] bucket = new int[high - low + 1];
        for (int d = 1; d <= digit; d++) {
            // 计数数组
            int[] count = new int[bucketSize];
            for (i = 0; i < arr.length; i++) {
                // 获取arr[i]第d位上的值
                j = getDigit(arr[i], d);
                // 放入到count数组中，计数加1
                count[j]++;
            }
            // 从第二个位置开始叠加值
            // 代表到含义：原本count[i]位置为当前位置值出现此时，此时修改为当前小于等于count[i]位置值到个数，并且count[i]的值一定小于arr数组长度
            for (i = 1; i < count.length; i++) {
                count[i] = count[i] + count[i - 1];
            }
            // 从右往左开始将值加入到bucket中
            // 实际满足：同一位置的值在bucket中先进先出
            for (i = high; i >= low; i--) {
                // 获取arr[i]第d位上的值
                j = getDigit(arr[i], d);
                // 根据d位置上的值从count数组中获取到值，并定位到需要放入bucket中到位置
                // 如：i=10,j=7,count[j] = 8,那么在bucket中存放到位置为bucket[7]
                // i=9,j=7,count[j]=7,那么在bucket中存放到位置为bucket[6]
                bucket[count[j] - 1] = arr[i];
                count[j]--;
            }
            // bucket数组从左到右将值放入arr[low,high]位置上
            for (i = low, j = 0; i <= high; i++, j++) {
                arr[i] = bucket[j];
            }
        }
    }

    /**
     * 获取数组中最大值，有多少个十位
     */
    private static int maxBits(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length - 1; i++) {
            max = Math.max(arr[i], max);
        }
        int index = 0;
        while (max != 0) {
            index++;
            max /= 10;
        }
        return index;
    }

    /**
     * 获取x第d位上的值
     * <p>
     * {@link Math#pow(double, double))}方法：获取某个数的n次方，第一个参数为底数，第二个参数为指数
     * </p>
     */
    public static int getDigit(int x, int d) {
        return ((x / ((int) Math.pow(10, d - 1))) % 10);
    }
}
