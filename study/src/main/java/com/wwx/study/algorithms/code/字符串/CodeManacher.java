package com.wwx.study.algorithms.code.字符串;

/**
 * Manacher算法
 * <p>
 * 解决问题: 找出字符串中最大回文串
 * <p>
 * 概念：
 * 1. 回文半径: 整个区域的一半
 * 2. 回文直径: 整个区域大小
 * 3. 最右回文边界: R, 之前所有字符回文区域所到达的最右边界
 * 4. 最右回文中心点: C, 取得最右回文边界时的中心点
 * <p>
 * 算法思想:
 * 1. 如果i >= R, 直接找i位置的最大回文半径, 并更新R位置
 * 2. 如果i < R
 * 2.1 如果i的对称位置i'的回文区域在以C为中心点的回文区域内, 则i位置的回文半径 = i'位置的回文半径
 * 2.2 如果i的对称位置i'的回文区域部分在C为中心点的回文区域内, 则i位置的回文半径 = R - i
 * 2.3 如果i的对称位置i'的回文区域左边界在C为中心点的回文区域内, 则i位置已知回文半径 = i'位置的回文半径,
 * 且需要判断R位置后的字符是否也属于回文串, 如果R位置后的字符也属于i位置的回文串, 需要更新R位置
 * <p>
 * 时间复杂度：O(n)
 * </p>
 *
 * @author wuwenxi 2022-09-06
 */
public class CodeManacher {

    public static int codeManacher(String original) {
        if (original == null || original.length() == 0) {
            return -1;
        }
        char[] manacher = manacher(original);
        // 回文半径数组
        int[] pArr = new int[manacher.length];
        // 回文右边界再往右一个位置, 回文有效区域R-1
        int R = -1,
                // 回文字符中心
                C = -1,
                // 最长回文字符长度
                max = Integer.MIN_VALUE;
        for (int i = 0; i != manacher.length; i++) {
            // i位置至少包含的回文半径
            // 1. 如果是i >= R情况, 至少为1, 可以和自己做回文
            // 2. 如果i < R情况, 回文半径至少为i'位置的回文半径或R-i, 取二者小者为回文半径
            pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1;
            // 在已知回文半径情况下, 继续向外判断是否有相等字符串
            // 在1和2.3情况下会继续向比较字符串
            // 在2.2和2.3 情况下跳出循环
            while (i + pArr[i] < manacher.length && i - pArr[i] > -1) {
                // 比较i位置左右字符
                if (manacher[i + pArr[i]] == manacher[i - pArr[i]]) {
                    pArr[i]++;
                } else {
                    break;
                }
            }
            // 如果i位置回文半径超过了R, 需要更新R及中心点C位置
            if (i + pArr[i] > R) {
                R = i + pArr[i];
                C = i;
            }
            // 更新最大回文半径
            max = Math.max(max, pArr[i]);
        }
        // 处理串半径-1与原串长度相等
        // #1#2#1# ==> max = 4, 原回文串长度为3
        return max - 1;
    }

    private static char[] manacher(String str) {
        char[] charArray = str.toCharArray();
        // 字符串
        char[] chars = new char[str.length() * 2 + 1];
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            // 奇数位的都是#
            // 偶数位都是原字符
            chars[i] = (i & 1) == 0 ? '#' : charArray[index++];
        }
        return chars;
    }
}
