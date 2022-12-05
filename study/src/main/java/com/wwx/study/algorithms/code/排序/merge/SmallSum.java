package com.wwx.study.algorithms.code.排序.merge;

/**
 * 在一个数组中，每一个数左边比当前数小的数累加起来，叫做这个数组的小和。求一个数组的小和。
 * 例如：
 * 对于数组[1,3,4,2,5]
 * 1左边比1小的数，没有；
 * 3左边比3小的数，1；
 * 4左边比4小的数，1、3；
 * 2左边比2小的数，1；
 * 5左边比5小的数，1、3、4、2；
 * 所以小和为1+1+3+1+1+3+4+2=16
 *
 * <p>
 * 基本思想：归并排序
 * </p>
 *
 * @author wuwenxi 2022-03-25
 */
public class SmallSum {

    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 2, 5};
        SmallSum smallSum = new SmallSum();
        int sum = smallSum.mergeSort(arr, 0, arr.length - 1);
        System.out.println(sum);
    }

    public int mergeSort(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        int mid = l + ((r - l) >> 1);
        return mergeSort(arr, l, mid) + mergeSort(arr, mid + 1, r) + merge(arr, l, mid, r);
    }

    private int merge(int[] arr, int l, int mid, int r) {
        int[] temp = new int[r - l + 1];
        int p = 0, pl = l, pr = mid + 1, result = 0;
        while (pl <= mid && pr <= r) {
            // (r - pr -1)的原因：右边的数都是有序的，r - pr + 1得到的结果是比pl位置上的值大的数量
            result += arr[pl] < arr[pr] ? (r - pr + 1) * arr[pl] : 0;
            //
            temp[p++] = arr[pl] < arr[pr] ? arr[pl++] : arr[pr++];
        }
        while (pr <= r) {
            temp[p++] = arr[pr++];
        }
        while (pl <= mid) {
            temp[p++] = arr[pl++];
        }
        if (temp.length >= 0) {
            System.arraycopy(temp, 0, arr, l, temp.length);
        }
        return result;
    }
}
