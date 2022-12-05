package com.wwx.study.algorithms.code.查找;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 输入两个字符串，找出a字符串中包含b字符串的最短字符串，
 * 不需要考虑字符顺序，只需要找到a中包含b最短的字符串
 * <p>
 * 如：a字符串：abcdef,b字符串：db
 * 输出：bcd
 * </p>
 *
 * @author wuwenxi 2022-03-23
 */
public class MinStr {

    public static void main(String[] args) {
        String s = minWindow("abcdef", "db");
        System.out.println(s);
    }

    /**
     * 滑动窗口
     * 1.将target字符串按字符的形式保存，并记录每个字符出现的次数
     * 2.定义两个指针来定位查询的字符位置，并定义字符长度
     * 3.移动指针，直到等于source字符串长度
     * 3.1 记录走过的全部字符，并记录字符出现的次数
     * 3.2 当右指针 - 左指针大于了target字符串长度或走过的字符已经全部包含target中的字符
     * 3.3 计算
     */
    private static String minWindow(String source, String target) {
        if (!StringUtils.hasLength(source)
                || !StringUtils.hasLength(target)
                || target.length() > source.length()) {
            return "";
        }
        Map<Character, Integer> needs = new HashMap<>();
        for (char ch : target.toCharArray()) {
            int nums = needs.getOrDefault(ch, 0);
            needs.put(ch, nums + 1);
        }
        int sLength = source.length(), tLength = target.length(), left = 0, right = 0, resLeftRight = Integer.MAX_VALUE, minLeft = 0;
        Map<Character, Integer> windows = new HashMap<>();
        while (right < sLength) {
            char curChar = source.charAt(right);
            int curNum = windows.getOrDefault(curChar, 0);
            windows.put(curChar, curNum + 1);
            right++;
            while (right - left >= tLength && check(windows, needs)) {
                int curLeftRight = right - left;
                if (curLeftRight < resLeftRight) {
                    //可能有很多符合条件的，比较出left、right间隔最小的
                    minLeft = left;
                    resLeftRight = curLeftRight;
                }
                char leftChar = source.charAt(left);
                int leftCharNum = windows.get(leftChar);
                if (leftCharNum == 1) {
                    windows.remove(leftChar);
                } else {
                    windows.put(leftChar, leftCharNum - 1);
                }
                left++;
            }
        }
        return resLeftRight == Integer.MAX_VALUE ? "" : source.substring(minLeft, minLeft + resLeftRight);
    }

    private static boolean check(Map<Character, Integer> map, Map<Character, Integer> targetMap) {
        if (map.size() < targetMap.size()) {
            return false;
        }
        for (Map.Entry<Character, Integer> entry : targetMap.entrySet()) {
            if (!map.containsKey(entry.getKey()) || map.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
}
