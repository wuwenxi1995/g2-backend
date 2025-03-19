package com.wwx.study.algorithms.code.排序;

import java.util.Arrays;
import java.util.UUID;

/**
 * 给你一个非负整数数组 nums 。如果存在一个数 x ，使得 nums 中恰好有 x 个元素 大于或者等于 x ，那么就称 nums 是一个 特殊数组 ，而 x 是该数组的 特征值
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/special-array-with-x-elements-greater-than-or-equal-x
 *
 * <p>
 * 输入：nums = [3,5]
 * 输出：2
 * 解释：有 2 个元素（3 和 5）大于或等于 2 。
 * </p>
 *
 * @author wuwenxi 2022-03-24
 */
public class SpecialArray {

    public static void main(String[] args) {
//        List<Integer> list1 = new ArrayList<>(Arrays.asList(1,2,8,9,4,6));
//        List<Integer> list2 = new ArrayList<>(Arrays.asList(1,2,8,9,4,6));
//        list1.sort(Comparator.comparing(Integer::intValue).reversed());
//        System.out.println(list1.toString());
//        Collections.sort(list2);
//        System.out.println(list2.toString());
//        // System.out.println(specialArray(new int[]{0, 0}));
        String token = String.format("%s-%s", UUID.randomUUID().toString(), Long.toHexString(System.currentTimeMillis()));
        System.out.println(token);
    }

    /**
     * 暴力解题：
     */
    private static int specialArray1(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int count = 0;
            for (int num : nums) {
                if (num >= i) {
                    count++;
                    if (count > i) {
                        break;
                    }
                }
            }
            if (count == i) {
                return i;
            }
        }
        return -1;
    }

    private static int specialArray(int[] nums) {
        Arrays.sort(nums);
        if (nums[0] >= nums.length) {
            return nums.length;
        }
        for (int i = 1; i <= nums.length; i++) {
            if (nums[nums.length - i] >= i && nums[nums.length - i - 1] < i) {
                return i;
            }
        }
        return -1;
    }

    private static int specialArray0(int[] nums) {
        int maxVal = 0;
        for (int num : nums) {
            if (num > maxVal) {
                maxVal = num;
            }
        }
        int[] counts = new int[maxVal + 1];
        for (int num : nums) {
            counts[num] += 1;
        }

        int sum = 0;
        for (int i = maxVal; i >= 0; i--) {
            sum += counts[i];
            if (sum == i) {
                return i;
            }
        }
        return -1;
    }
}
