package com.wwx.study.algorithms.code.并查集;

/**
 * 给定一个全部存放0和1的二维数组, 请找出二维数组中1连成一片到区域有多少
 * <p>
 * https://leetcode.cn/problems/number-of-islands/
 *
 * @author wuwenxi 2022-07-28
 */
public class Island {

    /**
     * 单核处理
     */
    public int island(int[][] m) {
        if (m == null || m[0] == null) {
            return 0;
        }
        int al = m.length, bl = m[0].length, res = 0;
        for (int i = 0; i < al; i++) {
            for (int j = 0; j < bl; j++) {
                if (m[i][j] == 1) {
                    res++;
                    infect(m, i, j, al, bl);
                }
            }
        }
        return res;
    }

    private void infect(int[][] m, int i, int j, int al, int bl) {
        if (i < 0 || i > al || j < 0 || j > bl || m[i][j] != 1) {
            return;
        }
        m[i][j] = 2;
        infect(m, i + 1, j, al, bl);
        infect(m, i - 1, j, al, bl);
        infect(m, i, j + 1, al, bl);
        infect(m, i, j - 1, al, bl);
    }

    /**
     * 并行处理
     *
     * <p>
     * 并查集处理, 将集合分为多个区域, 记录区域边缘有修改的点
     * </p>
     */
    public int numIsland(int[][] m) {
        if (m == null || m[0] == null) {
            return 0;
        }
        int processors = Runtime.getRuntime().availableProcessors();
        return -1;
    }

}
