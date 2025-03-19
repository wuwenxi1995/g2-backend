package com.wwx.study.algorithms.code.操作符;

/**
 * 给定一个非空整数数组，除了某两个元素只出现一次以外，其余每个元素均出现两次。找出这两个出现了一次的元素
 *
 * <p>
 * 输入: [2,2,1,4]
 * 输出: 1,4
 * </p>
 *
 * <p>
 * 思路：
 * 1. 先循环数组值做^运算，得到的值一定为两个值的亦或值，a^b
 * 2. a^b一定不为0
 * 2.1 说明a和b二进制某一位或多位不同，且一个为1一个0
 * 2.2 通过计算可以找到最右边的1 ==>  通过 c & (~c + 1) 可以得到c最右边的1
 * 如：c = 101011101 ==> 101011101 & (010100010 + 1) ==> 101011101 & 010100011 ==> 000000001
 * 3. 通过计算得到a^b最右边1的值与数组值做与运算
 * 3.1 得到的一定是0或非0的数
 * 3.2 主要是为了将数组中的值进行分类，a和b一定不是同一类
 * 3.3 使用这个数和数组筛选的值再进行亦或得到的一定是a或b中的一个
 * 4. 用3.3得到的值与a^b再次亦或，得到的一定是a或b
 *
 * @author wuwenxi 2022-03-24
 */
public class TwoSingleNumber {

//    public static void main(String[] args) {
//        TwoSingleNumber tsn = new TwoSingleNumber();
//        tsn.twoSingleNumber(new int[]{2, 2, 1, 4, 1, 3, 3, 5, 6, 4, 6, 7});
//    }

    public void twoSingleNumber(int[] nums) {
        int eor = 0;
        for (int num : nums) {
            eor ^= num;
        }
        // eor 一定为两个独数的亦或结果
        // eor 一定不为0 且两个独数的二进制某一位一定不同，且一个为1 一个为0
        // 通过 eor & (~eor + 1) 得到两个独数最右边不同位的值
        int rightNum = eor & (~eor + 1);
        // 第二次亦或
        int other = 0;
        for (int num : nums) {
            // 用数组值与rightNum做与运算，得到的一定是0或非0的数
            // 也就是将数组中的值分类，a和b一定是同一类，
            // 所以这个地方一定能亦或出a或b中的一个
            if ((num & rightNum) == 0) {
                other ^= num;
            }
        }
        System.out.println("第一个数：" + other + ",第二个数：" + (eor ^ other));
    }

}
