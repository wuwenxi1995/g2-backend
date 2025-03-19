package com.wwx.study.algorithms.practice.leetcode;

/**
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 * <p>
 * 说明：
 * <p>
 * 你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 * <p>
 * 示例 1:
 * <p>
 * 输入: [2,2,1]
 * 输出: 1
 * 示例 2:
 * <p>
 * 输入: [4,1,2,1,2]
 * 输出: 4
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/single-number
 *
 * <p>
 * ^(亦或)：不同为1，相同为0；可以理解为无进位运算
 * 如：10110 ^ 00111 = 10001
 * 特性：
 * 1. 0和任何数^得到这个数本身(0 ^ N = N); 任何数和自身做^得到0(N ^ N = 0)
 * 2. 满足交换律和结合律：a^b = b^a; a^b^c = (a^b)^c=a^(b^c)
 * 3. 由第二个特点可以得到：多个数做亦或，无论哪些数组合做亦或，最终得到的一定的是相同的结果
 * 例：两个数通过亦或做交换 a = 10，b = 12
 * a = a ^ b;  ==> a = 10 ^ 12
 * b = a ^ b;  ==> b = 10 ^ 12 ^ 12 ==> b = 10 ^ 0 ==> b = 10
 * a = a ^ b;  ==> a = 10 ^ 12 ^ 10 ==> a = (10 ^ 10) ^ 12 ==> a = 0 ^ 12 ==> a = 12
 * </p>
 *
 * @author wuwenxi 2022-03-24
 */
public class SingleNumber {

    private void singleNumber(int[] nums) {
        int num = 0;
        for (int value : nums) {
            num ^= value;
        }
        System.out.println(num);
    }
}
