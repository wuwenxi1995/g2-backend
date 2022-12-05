package com.wwx.study.algorithms.code.暴力递归;

/**
 * 给定一个整型数组arr，代表数值不同的纸牌；玩家A和玩家B依次拿走纸牌，
 * 每次都可以从最左或最右拿走纸牌，返回最终获胜者分数
 *
 * @author wuwenxi 2022-07-25
 */
public class CardInLine {

    public int win1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return Math.max(fir(arr, 0, arr.length - 1), sec(arr, 0, arr.length - 1));
    }

    private int fir(int[] arr, int i, int j) {
        if (i == j) {
            return arr[i];
        }
        return Math.max(arr[i] + sec(arr, i + 1, j), arr[j] + sec(arr, i, j - 1));
    }

    private int sec(int[] arr, int i, int j) {
        if (i == j) {
            return 0;
        }
        return Math.min(arr[i] + fir(arr, i + 1, j), arr[j] + fir(arr, i, j - 1));
    }

    //
    // 动态规划
    //

    public int win2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 先手
        int[][] f = new int[arr.length][arr.length];
        // 后手
        int[][] s = new int[arr.length][arr.length];
        for (int j = 0; j < arr.length; j++) {
            f[j][j] = arr[j];
            for (int i = j - 1; i >= 0; i--) {
                f[i][j] = Math.max(arr[i] + s[i + 1][j], arr[j] + s[i][j - 1]);
                s[i][j] = Math.min(f[i + 1][j], f[i][j - 1]);
            }
        }
        return Math.max(f[0][arr.length - 1], s[0][arr.length - 1]);
    }
}