package com.wwx.study.algorithms.code.排序;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 堆排序：完全二叉树
 * <p>
 * 基本概念：
 * 大根堆：值大的作为根结点
 * 小根堆：值小的作为根结点
 * 上浮、下沉：结点向上移动或向下移动
 * 一个结点的左子树位置可以通过公式(2 * i + 1)得到；
 * 一个结点的右子树位置可以通过公式(2 * (i + 1))得到；
 * 一个结点的父结点位置可以通过公式((i - 1) / 2)得到；
 * </p>
 *
 * <p>
 * 算法思想：
 * 1. 将数组生成大根堆
 * 2. 定义heapSize为数组长度，
 * 3. 将最大值和最小值做交换
 * 4. heapSize减一，并执行heapify
 * 4.1 定义index=0；判断index位置是否有左子树，要求左子树位置小于heapSize
 * 4.2 判断是否存在右子树，要求右子树位置小于heapSize；判断左子树与右子树值大小，取值最大的位置
 * 4.3 判断左右子树最大值与index位置的值的大小，如果都不比index位置的值大，进入第5步
 * 4.4 如果左右子树最大值比index位置的值大，交换index位置的值与左右子树最大值；index = 最大值位置
 * 5. 当heapSize>0时继续执行3、4步骤
 * </p>
 *
 * <p>
 * 时间复杂度：O(nlogn)
 * 空间复杂度：O(1)
 * </p>
 * <p>
 * jdk中的堆排序{@link PriorityQueue}，并且PriorityQueue默认是小根堆
 *
 * @author wuwenxi 2022-03-28
 */
public class Code_HeapSort {

    public static void main(String[] args) {
        int[] arr = {0, 3, 5, 6, 4, 2, 7, 8, 0, 5, 5, 3, 9, 5};
        Code_HeapSort sort = new Code_HeapSort();
        sort.heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public void heapSort(int[] arr) {
        // 先生成大根堆
//        for (int i = 1; i < arr.length; i++) {
//            heapInsert(arr, i);
//        }
        // [9, 8, 7, 6, 5, 5, 0, 3, 0, 4, 5, 3, 2, 5]
        for (int length = (arr.length - 1) >>> 1; length >= 0; length--) {
            heapify(arr, length, arr.length - 1);
        }
        int heapSize = arr.length - 1;
        while (heapSize > 0) {
            // 第一个值和heapSize位置的值做交换，并且做heapSize--，
            // 主要目的：大根堆第一个数为最大值，最后一个数为最小值；
            // 循环执行第一个数与heapSize做交换，实际是进行了排序
            swap(arr, 0, heapSize--);
            // 对结点做上浮下移操作；
            // 将[0 , heapSize]位置的最大值移动到最前面；最小值移动到最后
            heapify(arr, 0, heapSize);
        }
    }

    private void heapInsert(int[] arr, int index) {
        int parent;
        while (index > 0) {
            parent = (index - 1) >>> 1;
            if (arr[index] <= arr[parent]) {
                break;
            }
            swap(arr, index, parent);
            index = parent;
        }
    }

    private void heapify(int[] arr, int index, int heapSize) {
        int left = 2 * index + 1;
        while (left < heapSize) {
            // 如果存在右子树，取左右子树值最大的位置
            int largest = left + 1 < heapSize ? (arr[left + 1] > arr[left] ? left + 1 : left) : left;
            // 比较index位置的值和左右子树的值，取最大值的位置
            largest = arr[largest] > arr[index] ? largest : index;
            // 如果index位置已经是最大，结束循环
            if (largest == index) {
                break;
            }
            // 交换最大值位置的值和index位置上的值
            swap(arr, largest, index);
            // 从最新位置继续进行判断
            index = largest;
            left = 2 * index + 1;
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
