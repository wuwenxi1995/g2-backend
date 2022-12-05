package com.wwx.study.algorithms.code.暴力递归;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/permutations/
 * <p>
 * 全排列
 *
 * @author wuwenxi 2022-07-25
 */
public class PrintAllPermutations {

    public List<List<Integer>> permute(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        List<List<Integer>> res = new ArrayList<>();
        process(nums, 0, res);
        return res;
    }

    private void process(int[] nums, int i, List<List<Integer>> res) {
        if (i == nums.length) {
            List<Integer> list = new ArrayList<>();
            for (int num : nums) {
                list.add(num);
            }
            res.add(list);
            return;
        }
        for (int j = i; j < nums.length; j++) {
            swap(nums, i, j);
            process(nums, i + 1, res);
            swap(nums, j, i);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    private static List<String> permute(String s) {
        if (s == null || "".equals(s)) {
            return new ArrayList<>();
        }
        List<String> res = new ArrayList<>();
        process(s.toCharArray(), 0, res);
        return res;
    }

    private static void process(char[] str, int i, List<String> res) {
        if (i == str.length) {
            res.add(String.valueOf(str));
            return;
        }
        for (int j = i; j < str.length; j++) {
            swap(str, i, j);
            process(str, i + 1, res);
            swap(str, j, i);
        }
    }

    private static void swap(char[] str, int a1, int a2) {
        char temp = str[a1];
        str[a1] = str[a2];
        str[a2] = temp;
    }
}
