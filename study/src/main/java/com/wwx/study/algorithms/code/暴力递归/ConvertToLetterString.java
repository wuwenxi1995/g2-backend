package com.wwx.study.algorithms.code.暴力递归;

/**
 * 给定字符串111,可以转换成AAA,Ak,KA
 * 求字符str有多少种转换
 *
 * @author wuwenxi 2022-07-25
 */
public class ConvertToLetterString {

    public int convert(String str) {
        if (str == null || "".equals(str)) {
            return 0;
        }
        return process(str.toCharArray(), 0);
    }

    private int process(char[] str, int i) {
        if (i == str.length) {
            return 1;
        }
        if (str[i] == '0') {
            return 0;
        }
        if (str[i] == '1') {
            // 第一种情况, i字符单独转换
            int res = process(str, i + 1);
            // 第二种情况, (i和i+1)字符一起转换
            if (i + 2 < str.length) {
                res += process(str, i + 2);
            }
            return res;
        } else if (str[i] == '2') {
            // 第一种情况, i字符单独转换
            int res = process(str, i + 1);
            // 第二种情况, 满足i+1字符在0~6之间, (i和i+1)字符一起转换
            if (i + 1 < str.length &&
                    (str[i + 1] >= '0' && str[i + 1] <= '6')) {
                res += process(str, i + 2);
            }
            return res;
        }
        // 3～9 继续
        return process(str, i + 1);
    }
}
