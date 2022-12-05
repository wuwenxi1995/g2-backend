package com.wwx.study.algorithms.code.暴力递归;

/**
 * N皇后问题
 * <p>
 * 给定N个皇后棋子，在N * N的棋盘中，要求任意两个皇后不同行、不同列、不共斜线，求共有多少摆法
 * </p>
 * 如：N = 1, 输出1;
 * N = 2 或 N = 3, 输出0;
 * N = 8, 输出92
 * </p>
 *
 * @author wuwenxi 2022-07-20
 */
public class NQueens {

    //
    //
    // 递归

    private int nQueens(int n) {
        if (n < 1) {
            return 0;
        }
        // 一维数组记录皇后位置
        // 数组下标记录纵坐标, 数组值记录横坐标
        int[] arr = new int[n];
        // 递归回溯结果
        return process(0, arr, n);
    }

    private int process(int i, int[] arr, int n) {
        if (i == n) {
            return 1;
        }
        int res = 0;
        for (int j = 0; j < n; j++) {
            // 第i行，第j列和之前的(0..i-1)行皇后不共行不共列不共斜线
            // 如果是，认为有效
            // 如果不是，认为无效
            if (isValid(arr, i, j)) {
                arr[i] = j;
                res += process(i + 1, arr, n);
            }
        }
        return res;
    }

    /**
     * @param arr 皇后位置
     * @param i   纵坐标
     * @param j   横坐标
     */
    private boolean isValid(int[] arr, int i, int j) {
        // 1. 纵坐标一定不等
        for (int k = 0; k < i; k++) {
            // 2. 横坐标位置相等, 不符合不同列条件
            // 3.(两个位置纵坐标相减绝对值 = 两个位置横坐标位置相减绝对值) 不符合不共斜线
            if (j == arr[k] || Math.abs(j - arr[k]) == Math.abs(i - k)) {
                return false;
            }
        }
        return true;
    }

    //
    //
    // 二进制改进

    private int nQueens2(int n) {
        if (n < 1 || n > 32) {
            return 0;
        }
        // 二进制位
        int limit = n == 32 ? -1 : (n << 1) - 1;
        return process2(limit, 0, 0, 0);
    }

    /**
     * @param limit      二进制为,哪几位可以放
     * @param colLimit   列限制, 1代表不能放皇后, 0代表可放
     * @param leftLimit  左斜线限制, 1代表不能放皇后, 0代表可放
     * @param rightLimit 右斜线限制, 1代表不能放皇后, 0代表可放
     */
    private int process2(int limit, int colLimit, int leftLimit, int rightLimit) {
        if (colLimit == limit) {
            return 1;
        }
        // 找到可以皇后在二进制可以放的位置
        // 如：110001
        int pos = limit & (~(colLimit | leftLimit | rightLimit));
        int res = 0, maxRightOne;
        while (pos != 0) {
            // 找到pos最右边的1
            maxRightOne = pos & (~pos + 1);
            // 递归查找可以放的位置
            res += process2(limit,
                    // 列限制与最右边1做或运算, 得到最新已选位置
                    colLimit | maxRightOne,
                    // 左斜线限制位置与最右边1做或运算, 左移一位得到新的左斜线限制
                    (leftLimit | maxRightOne) << 1,
                    // 右斜线限制位置与最右边1做或运算, 右移一位得到新的右斜线限制
                    (rightLimit | maxRightOne) >>> 1);
            // 继续找下一个1
            pos = pos - maxRightOne;
        }
        return res;
    }

    //
    //
    // 动态规划

    public int nQueens3(int n) {
        if (n < 1) {
            return 0;
        }
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (isValid(dp, i, j)) {

                }
            }
        }
        return -1;
    }

    private boolean isValid(int[][] dp, int i, int j) {
        return true;
    }
}
