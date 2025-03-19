package com.wwx.study.algorithms.code.字符串;

/**
 * KMP算法
 * <p>
 * 解决问题：字符串a是否是字符串b的一个子串
 *
 * <p>
 * 算法思想:
 * 1. 前缀表
 * 1.1 前缀表(next数组), 用于指针回退, 当前缀与后缀不相等时, 前缀应该从哪个位置重新开始匹配;
 * 数组记录匹配串i位置之前的字符串前缀与后缀最大匹配长度, 如: aaabbaaabb, i=9时, 最大匹配长度为4;
 * 1.2 作用: 匹配串i位置与主串匹配失败, 通过i位置的最大匹配长度能够回退到之前已经过匹配的位置上
 * 1.3 前缀表实现:
 * 1.3.1 两个指针, 指针i指向next数组下标, 指针j记录i位置前最大匹配长度, 指针j也表示回退位置; i指针只能增加, j指针可以回退
 * 1.3.2 next数组前两个位置默认为-1和0, i指针从2开始且i<字符串长度, j指针从0开始
 * 1.3.3 如果i-1 和 j位置字符串相等, 可以得出结论: [i-j-1, i-1] 到 [0,j]位置字符串相等, i位置最大匹配长度 = i-1位置最大匹配长度 + 1(++j), i指针下移
 * 1.3.4 如果i-1 和 j位置字符串不相等, 如果j>0, 则j指针回退到next[j]位置, 重复1.3.3和1.3.4的操作
 * 1.3.5 如果如果i-1 和 j位置字符串不相等, 如果j<=0, 说明前缀和后缀没有相等的串, i位置最大匹配长度 = 0(j = 0), i继续指针下移
 *
 * <p>
 * 2. KMP
 * 2.1 得到匹配串的前缀表
 * 2.2 两个指针, 指向主串的s1和指向匹配串的s2; 满足s1<主串长度, s2指向位置<匹配串长度
 * 2.3 如果s1, s2指针位置字符相等, s1和s2指针下移
 * 2.4 如果s1, s2指针位置字符不想等, 且s2指针位置在next数组中的值不等于-1, 则s2指针回退到next[s2]字符位置,继续2.3和2.4操作
 * 2.5 如果s1, s2指针位置字符不想等, 且s2指针位置在next数组中的值等于-1, 则说明s2指针已经回退到0位置还不相等, s1指针下移, 重新开始匹配字符
 * 2.6 最后如果s2指向位置 = 匹配串长度, 说明主串与匹配串有匹配的字串, 否则说明没有匹配字串
 *
 * <p>
 * 时间复杂度: O(n)
 * </p>
 *
 * @author wuwenxi 2022-08-20
 */
public class CodeKmp {

    /**
     * 返回从主串哪个位置开始匹配的
     */
    public static int codeKmp(String str1, String str2) {
        if (str1 == null || str2 == null || str2.length() < 1 || str1.length() < str2.length()) {
            return -1;
        }
        char[] char1 = str1.toCharArray();
        char[] char2 = str2.toCharArray();
        int s1 = 0, s2 = 0;
        // 核心next数组
        int[] next = getNextArray(char2);
        // s1指针指向主串且s1<主串长度 或 s2 < 匹配串长度
        while (s1 < str1.length() && s2 < str2.length()) {
            // 如果s1,s2指针指向字符相等, 同时移动s1和s2指针
            if (char1[s1] == char2[s2]) {
                s1++;
                s2++;
            }
            // 如果s2指针在next数组中代表字符串2第一个字符, s1指针继续下一个字符, 重新开始匹配
            else if (next[s2] == -1) {
                s1++;
            }
            // s1指针不动，s2指针回溯到next数组中的位置
            else {
                s2 = next[s2];
            }
        }
        return s2 == str2.length() ? s1 - s2 : -1;
    }

    /**
     * 前缀表
     */
    private static int[] getNextArray(char[] str2) {
        int[] nextArr = new int[str2.length];
        nextArr[0] = -1;
        if (str2.length > 1) {
            nextArr[1] = 0;
            int i = 2 /*next数组下标*/, cn = 0/*哪个位置的字符和下标位置字符比较*/;
            while (i < nextArr.length) {
                // 如果前缀和后缀相等, cn+1, 并继续匹配下一个字符
                if (str2[i - 1] == str2[cn]) {
                    nextArr[i++] = ++cn;
                }
                // 如果当前位置前缀和后缀不相等, 匹配字符位置向前跳转
                else if (cn > 0) {
                    cn = nextArr[cn];
                }
                // 如果都不匹配, 则当前位置为0
                else {
                    nextArr[i++] = 0;
                }
            }
        }
        return nextArr;
    }
}