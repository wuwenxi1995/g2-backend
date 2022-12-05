package com.wwx.study.algorithms.code.排序;

import java.util.Arrays;

/**
 * 归并排序
 *
 * <p>
 * 基本思想：以二分 + 递归 + 合并的形式对数组排序；局部排序到全局有序
 * 1. 对数组进行二分，找到数组中间点:mid = l + (r - l) >> 1
 * 2. 按照找到的中间点，对左边和右边数组进行递归二分，直到不能再分
 * 3. 递归的同时需要进行对数值进行合并排序
 * 3.1 定义一个临时数组用于存储排好序的数组
 * 3.2 定义三个指针，第一个指针指向临时数组的存储位置，第二个指针指向[l,mid]中的位置，第三个指针指向[mid+1,r]中的位置
 * 3.3 只要第二个指针和第三个指针没有越界，一直比较第二个指针指向的值与第三个指针指向的值最小的值，并放入临时数组中
 * 3.3 当有一个指针越界，找到另一个未越界的指针，并将该指针指向的数组甚于内容存入临时数组中
 * 3.4 将临时数组中的值复制到原数组中第l个位置开始后的位置
 * </p>
 *
 * <p>
 * master公式：T(N) = a*(N/b) + O(N^d)
 * loga^b > d  ==> O(N^logb^a)   log以a为底数b为真数的对数值 > d，时间复杂度 = N的log以a为底数b为真数的对数值次幂
 * loga^b = d  ==> O(N*logN)     log以a为底数b为真数的对数值 = d，时间复杂度 = N * log以2为低N为真数低对数值
 * loga^b < d  ==> O(N^d)        log以a为底数b为真数的对数值 < d, 时间复杂度 = N的d次幂
 * <p>
 * 时间复杂度：O(N*logN)
 * 空间复杂度：O(N)
 * </p>
 *
 * @author wuwenxi 2022-03-25
 */
public class Code_MergeSort {

    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 2, 5};
        Code_MergeSort mergeSort = new Code_MergeSort();
        mergeSort.mergeSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public void mergeSort(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }
        // 求中点，使用L+(R-L)/2计算，使用位运算代替除法
        int mid = l + ((r - l) >> 1);
        mergeSort(arr, l, mid);
        mergeSort(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }

    private void merge(int[] arr, int l, int mid, int r) {
        // 创建一个临时数组
        int[] temp = new int[r - l + 1];
        // 定义一个指针指向新数组位置
        // 定义两个指针指向arr[l,mid]和arr[mid+1,r]
        int p = 0, pl = l, pr = mid + 1;
        // 当pl指针在[l,mid]范围并且pr指针在[mid+1,r]范围内时进行
        while (pl <= mid && pr <= r) {
            temp[p++] = arr[pl] <= arr[pr] ? arr[pl++] : arr[pr++];
        }
        // pl未越界或pr未越界，执行循环；
        // 如果pl和pr指针都越界，不知道以下循环
        while (pl <= mid) {
            temp[p++] = arr[pl++];
        }
        while (pr <= r) {
            temp[p++] = arr[pr++];
        }
        // 将临时数组中排好序的值copy到原数组中
        if (temp.length >= 0) {
            System.arraycopy(temp, 0, arr, l, temp.length);
        }
    }
}
