package com.wwx.study.algorithms.code.树.practice;

/**
 * 纸对折一次，出现的痕迹为凹痕，继续对折一次，出现的痕迹为凹凹凸，继续对折一次，出现的痕迹为凹凹凸凹凹凸凸;凹凹凸凹凹凸凸凹凹凹凸凸凹凸凸
 * 给定的一个数n，请打印出纸张对折n次出现的折痕
 *
 * <p>
 * 结论：每次对折后的折痕为左凹右凸，可以将痕迹视为一颗根结点为凹左子树为凹右子树为凸的二叉树，通过中序遍历即可得到从左到右的折痕
 * </p>
 *
 * @author wuwenxi 2022-04-27
 */
public class PaperFolding {

    public static void main(String[] args) {
        paperFolding(2);
    }

    public static void paperFolding(int n) {
        process(0, n, true);
    }

    private static void process(int num, int n, boolean down) {
        if (num >= n) {
            return;
        }
        process(num + 1, n, true);
        System.out.print(down ? "凹" : "凸");
        process(num + 1, n, false);
    }
}
