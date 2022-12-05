package com.wwx.study.algorithms.code.操作符;

import java.util.Arrays;

/**
 * 求一个数二进制最左和最右1位置的值
 * 如：1010
 * 输出：8，2
 * 如：00101100
 * 输出：32，4
 *
 * <p>
 * 基本思路
 * 求最左：将最左位1后面的数都变为1，将这个值+1后，得到的值为前一位为1，后面全是0，再右移一位即可得到最左1位置的值
 * 通过或运算可以得到
 * <p>
 * 求最右：通过对num求反+1再与num做与运算可以得到最右位1的值
 * </p>
 *
 * @author wuwenxi 2022-03-30
 */
public class MaxAndMinNumber {

    public static void main(String[] args) {
        int[] ints = maxAndMin(46);
        System.out.println(Arrays.toString(ints));
    }

    public static int[] maxAndMin(int num) {
        int max = findN(num);
        int min = num & (~num + 1);
        return new int[]{max, min};
    }

    private static int findN(int num) {
        int n = num - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n + 1) >> 1;
    }
}
