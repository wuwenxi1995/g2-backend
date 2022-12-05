package com.wwx.study.algorithms.code.排序.merge;

import java.time.Duration;

/**
 * 逆序对，在一个数组中，左边对数比右边的数大，则这两个数构成逆序对，找出有多少逆序对
 * 如：[3,2,4,5,0,1]
 * 输出：9 ((3,2),(3,0),(2,0),(4,0),(5,0),(3,1),(2,1),(4,1),(5,1))
 *
 * <p>
 * 基本思路：归并排序
 * </p>
 *
 * @author wuwenxi 2022-03-28
 */
public class ReverseOrder {

    public static void main(String[] args) {
        Duration parse = Duration.parse("3s");
        System.out.println(parse.toString());
//        int[] arr = {3, 2, 4, 5, 0, 1};
//        int reverse = reverse(arr, 0, arr.length - 1);
//        System.out.println(Arrays.toString(arr));
//        System.out.println(reverse);
    }

    private static int reverse(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        int mid = l + ((r - l) >> 1);
        return reverse(arr, l, mid) + reverse(arr, mid + 1, r) + merge(arr, mid, l, r);
    }

    private static int merge(int[] arr, int mid, int l, int r) {
        // 返回值
        int num = 0;
        // 临时数组
        int[] temp = new int[r - l + 1];
        int pt = 0, pl = l, pr = mid + 1;
        while (pl <= mid && pr <= r) {
            // 如果左边小于右边
            num = num + (arr[pl] > arr[pr] ? mid - pl + 1 : 0);
            temp[pt++] = arr[pl] <= arr[pr] ? arr[pl++] : arr[pr++];
        }
        while (pl <= mid) {
            temp[pt++] = arr[pl++];
        }
        while (pr <= r) {
            temp[pt++] = arr[pr++];
        }
        // 将临时数组中排好序的值copy到原数组中
        if (temp.length >= 0) {
            System.arraycopy(temp, 0, arr, l, temp.length);
        }
        return num;
    }
}
