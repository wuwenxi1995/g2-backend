package com.wwx.study.algorithms.code.暴力递归;

/**
 * 给定两个数组长度为N的数组, weight[N]和values[N], 给定限重bag
 * 在重量不超过bag的情况下, 求最大价值
 *
 * @author wuwenxi 2022-07-25
 */
public class Knapsack {

    public int maxValue1(int[] weight, int[] values, int bag) {
        return process(weight, values, 0, 0, bag);
    }

    private int process(int[] weight, int[] values, int i, int alreadyWeight, int bag) {
        if (i == weight.length) {
            return 0;
        }
        // 如果超重, 找有没有更轻的货物
        if (weight[i] + alreadyWeight > bag) {
            return process(weight, values, ++i, alreadyWeight, bag);
        }
        // 1.不要当前这个货物
        // 2.要当前这个货物
        // 得到最大值
        return Math.max(process(weight, values, i + 1, alreadyWeight, bag),
                values[i] + process(weight, values, i + 1, weight[i] + alreadyWeight, bag));
    }

    //
    //
    //  动态规划

    public int maxValue2(int[] weight, int[] values, int bag) {
        int[][] dp = new int[weight.length + 1][bag + 1];
        for (int i = weight.length - 1; i >= 0; i--) {
            for (int j = bag; j >= 0; j--) {
                dp[i][j] = dp[i + 1][j];
                if (j + weight[i] <= bag) {
                    dp[i][j] = Math.max(dp[i][j], values[i] + dp[i + 1][j + weight[i]]);
                }
            }
        }
        return dp[0][0];
    }
}
