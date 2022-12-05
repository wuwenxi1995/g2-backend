package com.wwx.study.algorithms.code.查找.binary;

/**
 * 二分查找
 * 算法思想：折半查找，每次查找最中间位置的值，与需要查找对值做对比
 * 如果中间位置的值等于查找值，直接返回；
 * 如果中间位置的值比查找的值大，继续对中间值左边的数组进行二分
 * 如果中间位置的值比查找的值小，继续对中间值右边的数组进行二分
 * 局限性：数组必须有序
 *
 * <p>
 * 时间复杂度：O(log2^n)  log以2为底数的对数
 * </p>
 *
 * @author wuwenxi 2022-03-25
 */
public class BinarySearch {
}
