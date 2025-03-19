package com.wwx.study.algorithms.code.暴力递归;

/**
 * 汉诺塔问题
 *
 * <p>
 * 给定N层汉诺塔, 打印最优解步数
 * <p>
 * 思路：大规模问题分解成小规模问题
 * 1. 1 ~ i-1 从from移动到other上
 * 2. i 从from移动到to上
 * 3. 1 ~ i-1 从other上移动到to上
 * </p>
 *
 * @author wuwenxi 2022-07-25
 */
public class Hanoi {

    public static void main(String[] args) {
        hanoi(2);
    }

    private static void hanoi(int n) {
        if (n > 0) {
            fun(n, "左", "右", "中");
        }
    }

    private static void fun(int k, String from, String to, String other) {
        if (k == 1) {
            System.out.println(">>> Move 1 from " + from + " to " + to);
        } else {
            fun(k - 1, from, other, to);
            System.out.println("<<< Move " + k + " form " + from + " to " + to);
            fun(k - 1, other, to, from);
        }
    }

}
