package com.wwx.study.algorithms.practice.leetcode;

/**
 * 给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
 * <p>
 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * <p>
 * 例如，121 是回文，而 123 不是。
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：x = 121
 * 输出：true
 * 示例 2：
 * <p>
 * 输入：x = -121
 * 输出：false
 * 解释：从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
 * 示例 3：
 * <p>
 * 输入：x = 10
 * 输出：false
 * 解释：从右向左读, 为 01 。因此它不是一个回文数。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/palindrome-number
 *
 * <p>
 * 基本思路：将输入值转为char字符，采用入栈出栈的方式判断是否回文
 * </p>
 *
 * @author wuwenxi 2022-03-31
 */
public class Palindromic {

    public static void main(String[] args) {
        System.out.println(isPalindrome(-121));
    }

    public static boolean isPalindrome(int x) {
        StringBuilder sb = new StringBuilder(String.valueOf(x));
        return sb.reverse().toString().equals(String.valueOf(x));
    }
}
